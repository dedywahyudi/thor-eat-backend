/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This class is for organization.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "ProductLine")
public class ProductLine extends IdentifiableEntity {
    @NotNull(groups = ValidationGroup.Create.class)
    @SafeHtml
    private String name;

    @NotNull(groups = ValidationGroup.Create.class)
    @SafeHtml
    private String divisionId;

    @SafeHtml
    private String subDivisionId;

    /**
     * The product line contacts.
     */
    @OneToMany(mappedBy = "productLine", cascade = CascadeType.ALL)
    private List<ProductLineContact> productLineContacts = new ArrayList<>();

    /**
     * Add contact to current product line.
     * @param contact the contact.
     */
	public void addContact(ProductLineContact contact) {
		contact.setProductLine(this);
		productLineContacts.add(contact);
	}
	/**
	 * Remove contact from current product line.
	 * @param contact the contact.
	 */
	public void removeContact(ProductLineContact contact) {
		productLineContacts.remove(contact);
		contact.setProductLine(null);
	}
}
