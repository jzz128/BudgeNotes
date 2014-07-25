package com.example.budgetnotebook;

public interface InputValidator {
	public static final String INPUT_REQUIRED = "Input is required.";
	public static final String EMAIL_REQUIRED = "Valid email is required.";
	public static final String NUMBER_REQUIRED = "Valid numeric value is required.";
	public static final String ALPHA_REQUIRED = "Field cannot contain numbers.";
	public static final String THIRTEEN_REQUIRED = "Must be at least 13.";
	public static final String FUTURE_BDAY = "Can't be born in the future";
	
	boolean inputsValid();
}
