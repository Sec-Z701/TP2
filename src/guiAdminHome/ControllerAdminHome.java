package guiAdminHome;

import database.Database;
import java.util.*;
import javafx.scene.control.*;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
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
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// The requirement that a prior invitation code hasn't been sent to the given email 
		// address has been removed because of the invitation codes expiring and the necessity to 
		// invite the same account tied to an email to multiple roles.
//		// Check to ensure that we are not sending a second message with a new invitation code to
//		// the same email address.  
//		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
//			ViewAdminHome.alertEmailError.setContentText(
//					"An invitation has already been sent to this email address.");
//			ViewAdminHome.alertEmailError.showAndWait();
//			return;
//		}
		
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		
		// If the 'None' role is selected, but the given email is not connected to an account,
		// inform the admin that new users must be assigned a role
		if (theSelectedRole == "None" && theDatabase.getUserName(emailAddress) == null) {
			ViewAdminHome.alertNewUserNotAssignedRole.showAndWait();
			return;
		}
		
		
		// Inform the user that the invitation has been sent and display the invitation code
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg;
		if (theSelectedRole == "None") {
			msg = "The code: " + invitationCode + " was sent to: " + emailAddress 
					+ " so that the user can update their username or password" 
					+ " to the associated account. It will expire after 24 hours.";
		}
		else {
			msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
					" was sent to: " + emailAddress + ". It will expire after 24 hours.";
		}
		System.out.println(msg);
		
		
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * 
	 *  This function gets the username from user input using a JavaFX TextInputDialog,
	 *  confirms with the user that they want to delete the user with the given username
	 *  by having them type 'yes' in another TextInputDialog, then deletes the user if they
	 *  are not an administrator and the input was confirmed with a 'yes'. Otherwise, gives an 
	 *  explanation of why user was not deleted.
	 *  
	 *  Re-uses ViewAdminHome.alertNotImplemented for cleanliness and maintainability.
	 *  
	 *  Tyler Benjamin 2/9
		
		// https://www.geeksforgeeks.org/java/javafx-textinputdialog/
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextInputDialog.html
		// https://www.baeldung.com/java-optional
	 */
	protected static void deleteUser() {
		
		// get username from user input
		TextInputDialog getUser = new TextInputDialog("Enter Username: ");
		getUser.setHeaderText("Enter username for deletion.");
		getUser.setContentText("Name: ");
		Optional<String> userName = getUser.showAndWait();
		String userString = userName.orElse("");
		
		// verify before deleting
		TextInputDialog verify = new TextInputDialog("Are you sure? Type 'Yes' to continue...");
		verify.setHeaderText("Are you sure?");
		verify.setContentText("Type 'yes' to proceed.");
		Optional<String> verOString = verify.showAndWait();
		String vString = verOString.orElse("");
		
		// if 'yes' not entered, fail. otherwise, delete user
		String successMessage = "Input not verified, user not deleted.";
		if (vString.toUpperCase().equals("YES")) {
			successMessage = theDatabase.deleteUser(userString);
		}

		ViewAdminHome.alertNotImplemented.setTitle("User Delete");
		ViewAdminHome.alertNotImplemented.setHeaderText("\n");
		ViewAdminHome.alertNotImplemented.setContentText(successMessage);
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * * Tyler Benjamin 2/9
		   Database.getUserList() (inside database pkg of foundationsSP26) returns a List<String> 
		   that begins with "<Select A User>", and then lists all users. We exclude the first element 
		   so we create the List<String> of all usernames, then get the account details and use the
		   pre-built alert to present it to the user in a simple, modular implementation that reuses 
		   previously implemented functions.
		   The modified userlist then calls a modified version of the provided getUserAccountDetails
		   (within the foundationsSP26 database pkg)
		   that returns a string with each of the user's details - username, name, email, roles.
		   
		   https://www.w3schools.com/java/java_list.asp
		*/
	protected static void listUsers() {
		
		// Get the userList, including the dummy header
		List<String> UserList = theDatabase.getUserList();
		// remove dummy header
		List<String> _UserList = UserList.subList(1, UserList.size());
		
		// get details for each user
		String content = "";
		for (String s : _UserList) {
			content += theDatabase.getUserAccountDetails2(s) + "\n\n";
		}
		
		// show user details
		System.out.println("\nListing users...\n");
		ViewAdminHome.alertNotImplemented.setTitle("\nUser List:");
		ViewAdminHome.alertNotImplemented.setHeaderText("Listing Current Users:\n");
		ViewAdminHome.alertNotImplemented.setContentText(content);
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
