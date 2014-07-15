package com.example.budgetnotebook;

public interface InputValidator {
	public static final String INPUT_REQUIRED = "Input is required.";
	public static final String EMAIL_REQUIRED = "Valid email is required.";
	
	boolean inputsValid();
}
