/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities.responses;

import java.util.List;

import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.ProductLineContact;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is the base class for division statistics.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class DivisionStatistics {
	/**
	 * the division is checked or not.
	 */
	private boolean checked;

	/**
	 * The divisiion.
	 */
	private Division division;

	/**
	 * The associated contacts.
	 */
	private List<ProductLineContact> contacts;
//	private List<Division> subDivisions;
}
