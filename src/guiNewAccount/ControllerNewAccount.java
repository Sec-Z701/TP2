package guiNewAccount;

import java.sql.SQLException;

import database.Database;
import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import passwordPopUpWindow.Model;

/*******
 * <p> Title: ControllerNewAccount Class. </p>
 * 
 * <p> Description: The Java/FX-based New Account Page.  This class provides the controller actions
 * to allow the user to establish a new account after responding to an invitation and the use of a
 * one time code.
 * 
 * The controller deals with the user pressing the "User Step" button widget being click.  If also
 * supports the user click on the "Quit" button widget.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

public class ControllerNewAccount {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	/**
	 * Default constructor is not used.
	 */
	public ControllerNewAccount() {
	}
	
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> Method: public doCreateUser() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the User Setup
	 * button.  This method checks the input fields to see that they are valid.  If so, it then
	 * creates the account by adding information to the database.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	protected static void doCreateUser() {
		
		// Fetch the username and password. (We use the first of the two here, but we will validate
		// that the two password fields are the same before we do anything with it.)
		String username = ViewNewAccount.text_Username.getText();
		String password = ViewNewAccount.text_Password1.getText();
		
		// Display key information to the log
		System.out.println("** Account for Username: " + username + "; theInvitationCode: "+
				ViewNewAccount.theInvitationCode + "; email address: " + 
				ViewNewAccount.emailAddress + "; Role: " + ViewNewAccount.theRole);
		
		// Initialize local variables that will be created during this process
		int roleCode = 0;
		User user = null;
		
		// Are we updating the login for an already created user?
		boolean updateExistingUser = ViewNewAccount.theRole.equals("None");
		
		// Is the given username already recorded for associated account?
		boolean sameUsername = updateExistingUser  && username.equals(theDatabase.getUserName(ViewNewAccount.emailAddress));
		
		// Make sure username is valid
		String usernameError = "";
		if (!sameUsername) {
			usernameError = checkForValidUserName(username);
		}
		if (usernameError.isEmpty()) {
		
			// Make sure the two passwords are the same.	
			if (ViewNewAccount.text_Password1.getText().
					compareTo(ViewNewAccount.text_Password2.getText()) == 0) {

				// Validate password using the Password FSM
				String pwError = Model.evaluatePassword(password);
				if (!pwError.isEmpty()) {
				    Alert alertPasswordInvalid = new Alert(AlertType.INFORMATION);
				    alertPasswordInvalid.setTitle("Invalid Password");
				    alertPasswordInvalid.setHeaderText(null);
				    alertPasswordInvalid.setContentText(pwError);
				    alertPasswordInvalid.showAndWait();

					// Make sure it is impossible to log in if Error triggers
				    ViewNewAccount.text_Password1.setText("");
				    ViewNewAccount.text_Password2.setText("");
				    return;
				}
				
				// Modify the username/password to an existing account
				if (updateExistingUser) {
					
					// Only update username if it's new
					if (!sameUsername) {
						theDatabase.updateUserName(ViewNewAccount.emailAddress, username);
					}
					theDatabase.updatePassword(ViewNewAccount.emailAddress, password);
					
					// The account has been updated, so remove the invitation from the system
		            theDatabase.removeInvitationAfterUse(ViewNewAccount.theInvitationCode);
		            
		            // Inform the user their account was successfully modified
		            ViewNewAccount.alertAccountLoginUpdated.setContentText(
		            		"The login information for the account associated with the email "
		            		+ ViewNewAccount.emailAddress + " has been updated.");
		            ViewNewAccount.alertAccountLoginUpdated.showAndWait();
		            
		            // Bring user back to login page
		            guiUserLogin.ViewUserLogin.displayUserLogin(ViewNewAccount.theStage);
				}
				
				// Create a new user
				else {
					// The passwords match so we will set up the role and the User object base on the 
					// information provided in the invitation
					if (ViewNewAccount.theRole.compareTo("Admin") == 0) {
						roleCode = 1;
						user = new User(username, password, "", "", "", "", "", true, false, false);
					} else if (ViewNewAccount.theRole.compareTo("Role1") == 0) {
						roleCode = 2;
						user = new User(username, password, "", "", "", "", "", false, true, false);
					} else if (ViewNewAccount.theRole.compareTo("Role2") == 0) {
						roleCode = 3;
						user = new User(username, password, "", "", "", "", "", false, false, true);
					} else {
						System.out.println(
								"**** Trying to create a New Account for a role that does not exist!");
						System.exit(0);
					}
					
					// Unlike the FirstAdmin, we know the email address, so set that into the user as well.
		        	user.setEmailAddress(ViewNewAccount.emailAddress);
		
		        	// Inform the system about which role will be played
					applicationMain.FoundationsMain.activeHomePage = roleCode;
					
		        	// Create the account based on user and proceed to the user account update page
		            try {
		            	// Create a new User object with the pre-set role and register in the database
		            	theDatabase.register(user);
		            } catch (SQLException e) {
		                System.err.println("*** ERROR *** Database error: " + e.getMessage());
		                e.printStackTrace();
		                System.exit(0);
		            }
		            
		            // The account has been set, so remove the invitation from the system
		            theDatabase.removeInvitationAfterUse(ViewNewAccount.theInvitationCode);
		            
		            // Set the database so it has this user and the current user
		            theDatabase.getUserAccountDetails(username);
		
		            // Navigate to the Welcome Login Page
		            guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewNewAccount.theStage, user);
				}
			}
			else {
				// The two passwords are NOT the same, so clear the passwords, explain the passwords
				// must be the same, and clear the message as soon as the first character is typed.
				ViewNewAccount.text_Password1.setText("");
				ViewNewAccount.text_Password2.setText("");
				ViewNewAccount.alertUsernamePasswordError.showAndWait();
			}
		}
		else {
			// The username is NOT valid. Send error message as an alert.
			Alert alertUsernameInvalid = new Alert(AlertType.INFORMATION);
			alertUsernameInvalid.setTitle("Invalid Username");
			alertUsernameInvalid.setHeaderText(null);
			alertUsernameInvalid.setContentText(usernameError);
			alertUsernameInvalid.showAndWait();
			
		}
	}
	
	/**********
	 * <p> Method: public checkForValidUserName() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the User Setup
	 * button.  This method checks to make sure that the user name has between 4 and 16 
	 * characters, and that it only has alphanumeric characters or allowed special characters. 
	 * It must start with an alphabetic character and end in an alphanumeric character.
	 * 
	 * @param input is a string that is the given username
	 * 
	 * @return the error message (if any) about what was wrong with the given username
	 * 
	 */	
	public static String checkForValidUserName(String input) {
		// Check to ensure that there is input to process
		if (input.length() <= 0) {
			return "The input is empty";
		}
		
		// Make sure that the username doesn't already exist
		if (theDatabase.doesUserExist(input)) {
			return "This username is already associated with an account";
		}
		
		// The local variables used to perform the Finite State Machine simulation
		int state = 0;							// This is the FSM state number
		String inputLine = input;				// Save the reference to the input line
		int currentCharNdx = 0;					// The index of the current character
		char currentChar = input.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		boolean running = true;					// Start the loop
		int nextState = -1;						// There is no next state
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		int userNameSize = 0;					// Initialize the UserName size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )) {	// Check for a-z
					nextState = 1;
					
					// Count the character 
					userNameSize++;
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z, 0-9 that transitions back to state 1
				//  2: a "-", "_", or "." that transitions to state 2 

				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the character
					userNameSize++;
				}
				// "-", "_", or "." -> State 2
				else if (currentChar == '.' ||	// Check for .
						currentChar == '-' || 	// Check for -
						currentChar == '_') {	// Check for _
					nextState = 2;
					
					// Count the character
					userNameSize++;
				}				
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
				
			case 2: 
				// State 2 deals with a character after a period, underscore, or hyphen in the name.
				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the odd digit
					userNameSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
			}
			
			if (running) {
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				currentCharNdx++;
				if (currentCharNdx < inputLine.length())
					currentChar = inputLine.charAt(currentCharNdx);
				else {
					currentChar = ' ';
					running = false;
				}

				// Move to the next state
				state = nextState;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		String userNameRecognizerErrorMessage = "";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage += "A UserName must start with alphabetic characters.\n";
			return userNameRecognizerErrorMessage;

		case 1:
			// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
			// we must ensure the whole string has been consumed.

			if (userNameSize < 4) {
				// UserName is too small
				userNameRecognizerErrorMessage += "A UserName must have at least 4 characters.\n";
				return userNameRecognizerErrorMessage;
			}
			else if (userNameSize > 16) {
				// UserName is too long
				userNameRecognizerErrorMessage += 
					"A UserName must have no more than 16 characters.\n";
				return userNameRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				userNameRecognizerErrorMessage += 
					"A UserName character may only contain the characters A-Z, a-z, 0-9, '.', '-', or '_'.\n";
				return userNameRecognizerErrorMessage;
			}
			else {
					// UserName is valid
					userNameRecognizerErrorMessage = "";
					return userNameRecognizerErrorMessage;
			}

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage +=
				"A UserName character after a period, hyphen, or underscore must be A-Z, a-z, 0-9.\n";
			return userNameRecognizerErrorMessage;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}

	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}
