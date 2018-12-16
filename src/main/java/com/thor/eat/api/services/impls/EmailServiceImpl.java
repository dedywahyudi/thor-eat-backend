package com.thor.eat.api.services.impls;

import com.thor.eat.api.aop.LogAspect;
import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.Standard;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.StandardDivisionSearchCriteria;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.requests.StandardDivisionsEmailRequest;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.DivisionService;
import com.thor.eat.api.services.EmailService;
import com.thor.eat.api.services.OrganizationService;
import com.thor.eat.api.services.StandardDivisionService;
import com.thor.eat.api.services.StandardService;
import com.thor.eat.api.services.UserService;
import com.thor.eat.api.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangjinggang on 2018/3/1.
 */
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * The java mail sender.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * The email template engine for body.
     */
    @Autowired
    private TemplateEngine bodyTemplateEngine;

    /**
     * The email template engine for subject.
     */
    @Autowired
    private TemplateEngine subjectTemplateEngine;

    @Autowired
    private StandardDivisionService standardDivisionService;;

    @Autowired
    private OrganizationService organizationService;

    /**
     * Represents the standard service.
     */
    @Autowired
    private StandardService standardService;

    /**
     * Represents the division service.
     */
    @Autowired
    private DivisionService divisionService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(javaMailSender, "javaMailSender");
        Helper.checkConfigNotNull(bodyTemplateEngine, "bodyTemplateEngine");
        Helper.checkConfigNotNull(subjectTemplateEngine, "subjectTemplateEngine");
    }

    /**
     * Send email with to email address, email fullName and model params.
     *
     * @param toEmails   the to email address.
     * @param ccEmails   the cc email address.
     * @param emailTemplate the email template name.
     * @param context     the model params.
     * @throws ThorEarlyException throws if error to send email.
     */
    public void sendEmail(List<String> toEmails, List<String> ccEmails, String emailTemplate, Context context)
            throws ThorEarlyException {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            helper.setTo(toEmails.toArray(new String[0]));
            if (ccEmails != null) {
                helper.setCc(ccEmails.toArray(new String[0]));
            }

            helper.setFrom(Helper.getAuthUser().getEmail());

            helper.setSubject(subjectTemplateEngine.process(emailTemplate, context));
            helper.setText(bodyTemplateEngine.process(emailTemplate, context), true);

            LogAspect.LOGGER.info("Email sends to " + String.join(", ", toEmails));
            javaMailSender.send(mail);
        } catch (MessagingException | MailException e) {
            throw new ThorEarlyException("Error to send email", e);
        }
    }

    @Override
    public void sendStandardDivisionsEmail(StandardDivisionsEmailRequest request) throws ThorEarlyException {

        // search for the standard divisions
        StandardDivisionSearchCriteria criteria = new StandardDivisionSearchCriteria();
        criteria.setIds(request.getStandardDivisionIds());
        List<StandardDivision> standardDivisions = standardDivisionService.search(criteria);

        // create the context
        Context context = new Context();
        context.setVariable("subject", request.getSubject());
        context.setVariable("bodyMessage", request.getBody());
        context.setVariable("standardDivisions", standardDivisions);

        sendEmail(request.getTo(), request.getCc(), "standard_divisions", context);
    }

    @Override
    public void sendStandardMutatedEmail(OperationType operationType, StandardDivision standardDivision, PendingStandard standard, User requestedUser) throws ThorEarlyException {
        List<String> emails = new ArrayList<>();

        // add user who are impacted by the standard
        // add standard participants
        emails.addAll(this.getAllImpectedEmailAddress(standardDivision));

        // add original user
        emails.add(requestedUser.getEmail());

        Organization org = organizationService.get(standard.getOrganization().getId());

        Context context = new Context();
        context.setVariable("standardDivision", standardDivision);
        context.setVariable("standard", standard);
        context.setVariable("organization", org);
        List<String> contacts = new ArrayList<>();
        if (standardDivision.getProductLine() != null) {
	        for (ProductLineContact contact: standardDivision.getProductLine().getProductLineContacts()) {
	        	contacts.add(contact.getContactEmail());
	        }
        }
        context.setVariable("contacts", String.join(",", contacts));
        String templateName;
        if (operationType == OperationType.Insert) {
            templateName = "standard_created";
        } else if (operationType == OperationType.Update) {
            templateName = "standard_updated";
        } else {
            throw new IllegalArgumentException("Only support Insert/Update operations");
        }
        if (!emails.isEmpty()) {
        	LogAspect.LOGGER.info("Email already sends to " + String.join(", ", emails));
            sendEmail(emails, null, templateName, context);
        }
    }

    /**
     * Send approved email.
     *
     * @param requestedUser the requested user.
     * @param standardDivision the associated standard division.
     * @param standardName the standard name.
     * @throws ThorEarlyException throws if error to send email.
     */
    public List<String> sendApprovedEmail(User requestedUser, StandardDivision standardDivision, String standardName) throws ThorEarlyException {
    	// Get all email address
    	List<String> emails = getAllImpectedEmailAddress(standardDivision);

        emails.add(requestedUser.getEmail());

    	Context context = new Context();
        context.setVariable("standardName", standardName);
        if (!emails.isEmpty()) {
        	LogAspect.LOGGER.info("Approved email already sends to " + String.join(", ", emails));
            sendEmail(emails, null, "approved", context);
        }
        return emails;
    }
    /**
     * Send reject email.
     *
     * @param requestedUser the requested user.
     * @param standardDivision the associated standard division.
     * @param standardName the standard name.
     * @throws ThorEarlyException throws if error to send email.
     */
    public List<String> sendRejectEmail(User requestedUser, StandardDivision standardDivision, String standardName, String rejectMessage) throws ThorEarlyException {
    	// Get all email address
    	List<String> emails = getAllImpectedEmailAddress(standardDivision);

        emails.add(requestedUser.getEmail());

        Context context = new Context();
        context.setVariable("standardName", standardName);
        context.setVariable("rejectMessage", rejectMessage);
        if (!emails.isEmpty()) {
        	LogAspect.LOGGER.info("Reject email already sends to " + String.join(", ", emails));
            sendEmail(emails, null, "reject", context);
        }
        return emails;
    }

    /**
     * Send cns issue mutated email. Only for insert operation.
     *
     * @param operationType the operation type.
     * @param issue the issue
     * @param requestedUser the requested user
     * @throws ThorEarlyException if any error occurs.
     */
    public void sendCnSIssueMutatedEmail(OperationType operationType, CnSIssue issue, User requestedUser) throws ThorEarlyException {
    	List<String> emails = Arrays.asList(requestedUser.getEmail());

    	Division division = divisionService.get(issue.getDivisionId());

    	if (!emails.isEmpty() && division != null) {
    		Context context = new Context();
    		context.setVariable("issue", issue);
    		if (issue.getStandardId() != null) {
    			context.setVariable("standard", standardService.get(issue.getStandardId()));
    		}
    		context.setVariable("division", division);

    		if (operationType == OperationType.Insert) {
    			this.sendEmail(emails, null, "cns_issue_created", context);
            } else {
                throw new IllegalArgumentException("Only support Insert operations");
            }
    	}
    }
    public void sendDivisionOrOrgMutatedEmail(OperationType operationType, PendingStandard standard, User requestedUser) throws ThorEarlyException {
    	List<String> emails = Arrays.asList(requestedUser.getEmail());

    	if (!emails.isEmpty() && standard != null) {
    		Context context = new Context();
    		context.setVariable("item", standard);

    		if (standard.getDivisionId() != null && standard.getDescription() != null) {
    			context.setVariable("parent", divisionService.get(standard.getDescription()));
    			context.setVariable("type", "Sub-Division");
    		} else if (standard.getDivisionId() != null) {
    			context.setVariable("type", "Division");
    		} else {
    			context.setVariable("type", "Organization");
    		}
    		if (operationType == OperationType.Insert) {
    			this.sendEmail(emails, null, "division_org_created", context);
    		} else if (operationType == OperationType.Update) {
    			this.sendEmail(emails, null, "division_org_updated", context);
    		} else if (operationType == OperationType.Delete) {
    			this.sendEmail(emails, null, "division_org_deleted", context);
    		}
    	}
    }
    /**
     * Get all impected user email address.
     * @param standardDivision the standard division.
     * @return the email addresses.
     */
    private List<String> getAllImpectedEmailAddress(StandardDivision standardDivision) {
    	List<String> emails = new ArrayList<>();

    	if (standardDivision != null) {
	    	emails.addAll(standardDivision.getStandardParticipant());
	
	    	if (standardDivision.getProductLine() != null) {
		        List<ProductLineContact> contacts = standardDivision.getProductLine().getProductLineContacts();
		        for (ProductLineContact contact : contacts) {
		        	String emailsStr = contact.getContactEmail();
		        	if (emailsStr != null && !emailsStr.isEmpty()) {
		        		// add all contact
		        		emails.addAll(Arrays.asList(emailsStr.split(" ")));
		        	}
		        }
	    	}
    	}
        return emails;
    }
}
