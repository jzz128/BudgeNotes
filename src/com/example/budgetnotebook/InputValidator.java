/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * InputValidator.java
 * 
 * Validation error messages.
 * 
 **/

package com.example.budgetnotebook;

public interface InputValidator {
	public static final String INPUT_REQUIRED = "Input is required.";
	public static final String EMAIL_REQUIRED = "Valid email is required.";
	public static final String NUMBER_REQUIRED = "Valid numeric value is required.";
	public static final String ALPHA_REQUIRED = "Field cannot contain numbers.";
	public static final String THIRTEEN_REQUIRED = "Must be at least 13.";
	public static final String FUTURE_BDAY = "Cannot be born in the future.";
	public static final String INVALID_DATE = "Enter MM/DD/YYYY format.";
	
	boolean inputsValid();
}