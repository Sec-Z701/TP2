package passwordEvaluationTestbedMain;

/*******
 * <p> Title: PasswordEvaluationTestingAutomation Class. </p>
 * 
 * <p> Description: A Java demonstration for semi-automated tests </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2022-02-25 A set of semi-automated test cases
 * @version 2.00	2024-09-22 Updated for use at ASU
 * 
 */
public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		// This is a properly written positive test
		performTestCase(1, "Aa!15678", true);
		
		// This is a properly written negative test
		performTestCase(2, "A!", false);
		
		//order doesn't matter
		performTestCase(3, "!@#123aB", true);
		
		//length 8 but still false
		performTestCase(4, "abcdefgh!", false);
		
		//Empty input
		performTestCase(5, "", false);
		
		// Missing Upper Case
		performTestCase(6, "ab1!5678", false);  
		
		// Missing Lower Case
		performTestCase(7, "AB1!5678", false);  
		
		// Missing Numeric
		performTestCase(8, "Abc!defg", false); 
		
		// Missing Special Char
		performTestCase(9, "Abc1defg", false);   
		
		// Length 7 (Too Short)
		performTestCase(10, "Ab1!567", false); 
		
		// Length 8 (Valid Boundary)
		performTestCase(11, "Ab1!5678", true); 
		
		// Contains Space (Invalid)
		performTestCase(12, "Ab1!567 ", false);  

		//Max Length Tests (Limit is 28)

		//Too long (32 characters) 
		performTestCase(13, "Aa!15678Aa!15678Aa!15678Aa!15678", false); 

		// Edge case (Length == 28)
		performTestCase(14, "Aa!15678Aa!15678Aa!15678Aa!1", true);      

		//Edge Case (Length < 29) 
		performTestCase(15, "Aa!15678Aa!15678Aa!15678Aa!15", false);
		
		
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
				
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText= passwordPopUpWindow.Model.evaluatePassword(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		
		if (passwordPopUpWindow.Model.foundUpperCase)
			System.out.println("At least one upper case letter - Satisfied");
		else
			System.out.println("At least one upper case letter - Not Satisfied");

		if (passwordPopUpWindow.Model.foundLowerCase)
			System.out.println("At least one lower case letter - Satisfied");
		else
			System.out.println("At least one lower case letter - Not Satisfied");
	

		if (passwordPopUpWindow.Model.foundNumericDigit)
			System.out.println("At least one digit - Satisfied");
		else
			System.out.println("At least one digit - Not Satisfied");

		if (passwordPopUpWindow.Model.foundSpecialChar)
			System.out.println("At least one special character - Satisfied");
		else
			System.out.println("At least one special character - Not Satisfied");

		if (passwordPopUpWindow.Model.foundLongEnough)
			System.out.println("At least 8 characters - Satisfied");
		else
			System.out.println("At least 8 characters - Not Satisfied");
	}
}
