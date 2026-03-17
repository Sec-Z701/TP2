package hw2app;

import guiPostsReplies.ControllerPostsReplies;
import guiPostsReplies.ModelPostsReplies;
import guiPostsReplies.ViewPostsReplies;
import javafx.application.Application;
import javafx.stage.Stage;

/*******
 * <p> Title: HW2Main Class </p>
 *
 * <p> Description: Standalone launcher for HW2 Posts/Replies GUI for testing. </p>
 *
 */
public class HW2Main extends Application {

	@Override
	public void start(Stage stage) {

		ModelPostsReplies model = new ModelPostsReplies();
		ViewPostsReplies view = new ViewPostsReplies();
		ControllerPostsReplies controller = new ControllerPostsReplies(model, view);

		view.setController(controller);
		view.show(stage);

		// Initial refresh
		controller.refreshLists();
	}

	public static void main(String[] args) {
		launch(args);
	}
}