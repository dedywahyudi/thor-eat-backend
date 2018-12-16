package com.thor.eat.api.services;

import java.util.List;

import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.requests.StandardDivisionsEmailRequest;
import com.thor.eat.api.exceptions.ThorEarlyException;

/**
 * Created by wangjinggang on 2018/3/1.
 */
public interface EmailService {
    void sendStandardDivisionsEmail(StandardDivisionsEmailRequest request) throws ThorEarlyException;

    void sendStandardMutatedEmail(OperationType operationType, StandardDivision standardDivision, PendingStandard standard, User requestedUser) throws ThorEarlyException;

    List<String> sendApprovedEmail(User requestedUser, StandardDivision standardDivision, String standardName) throws ThorEarlyException;

    List<String> sendRejectEmail(User requestedUser, StandardDivision standardDivision, String standardName, String rejectMessage) throws ThorEarlyException;

    void sendCnSIssueMutatedEmail(OperationType operationType, CnSIssue issue, User requestedUser) throws ThorEarlyException;

    void sendDivisionOrOrgMutatedEmail(OperationType operationType, PendingStandard standard, User requestedUser) throws ThorEarlyException;
}
