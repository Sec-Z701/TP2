package guiPostsReplies;

import java.util.ArrayList;

import entityClasses.Post;
import entityClasses.Reply;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewPostsReplies Class </p>
 *
 * <p> Description: Minimal JavaFX GUI to demonstrate HW2 Post/Reply CRUD, validation,
 * and subset list behavior. </p>
 *
 */
public class ViewPostsReplies {

	// UI components
	private TextField tfPostId = new TextField();
	private TextField tfPostAuthor = new TextField();
	private TextField tfPostThread = new TextField();
	private TextField tfPostTitle = new TextField();
	private TextArea  taPostBody = new TextArea();
	private TextField tfPostSearch = new TextField();
	private CheckBox  cbConfirmDelete = new CheckBox("Confirm delete (Are you sure?)");

	private ListView<String> lvAllPosts = new ListView<>();
	private ListView<String> lvSubsetPosts = new ListView<>();

	private TextField tfReplyId = new TextField();
	private TextField tfReplyPostId = new TextField();
	private TextField tfReplyAuthor = new TextField();
	private TextArea  taReplyBody = new TextArea();
	private TextField tfReplySearch = new TextField();

	private ListView<String> lvAllReplies = new ListView<>();
	private ListView<String> lvSubsetReplies = new ListView<>();

	private Label lblMessage = new Label("");

	// Controller reference
	private ControllerPostsReplies controller;

	/*****
	 * Launch this view in a new Stage
	 */
	public void show(Stage stage) {

		// Build UI
		VBox root = new VBox(12);
		root.setPadding(new Insets(12));

		lblMessage.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

		HBox top = new HBox(12,
				buildPostPane(),
				buildReplyPane()
		);

		root.getChildren().addAll(
				new Label("HW2 Posts / Replies Demo"),
				top,
				new Separator(),
				new Label("Messages / Errors:"),
				lblMessage
		);

		Scene scene = new Scene(root, 1100, 650);
		stage.setTitle("HW2 - Posts/Replies");
		stage.setScene(scene);
		stage.show();
	}

	// Called after model/controller are created
	public void setController(ControllerPostsReplies controller) {
		this.controller = controller;
	}

	// -------------------------
	// UI Sections
	// -------------------------

	private Pane buildPostPane() {

		tfPostId.setPromptText("PostId (for update/delete)");
		tfPostAuthor.setPromptText("Author username");
		tfPostThread.setPromptText("Thread (optional)");
		tfPostTitle.setPromptText("Title");
		taPostBody.setPromptText("Body");
		taPostBody.setPrefRowCount(4);

		tfPostSearch.setPromptText("Search keyword");

		Button btnCreate = new Button("Create Post");
		btnCreate.setOnAction(e -> controller.handleCreatePost());

		Button btnUpdate = new Button("Update Post");
		btnUpdate.setOnAction(e -> controller.handleUpdatePost());

		Button btnDelete = new Button("Delete Post");
		btnDelete.setOnAction(e -> controller.handleDeletePost());

		Button btnSearch = new Button("Search Posts");
		btnSearch.setOnAction(e -> controller.handleSearchPosts());

		Button btnClearSubset = new Button("Clear Post Subset");
		btnClearSubset.setOnAction(e -> controller.handleClearPostSubset());

		VBox inputs = new VBox(6,
				new Label("POST INPUTS"),
				tfPostId,
				tfPostAuthor,
				tfPostThread,
				tfPostTitle,
				taPostBody,
				cbConfirmDelete,
				new HBox(6, btnCreate, btnUpdate, btnDelete),
				new Separator(),
				new Label("POST SEARCH (subsetPosts)"),
				tfPostSearch,
				new HBox(6, btnSearch, btnClearSubset)
		);

		VBox lists = new VBox(6,
				new Label("All Posts (allPosts)"),
				lvAllPosts,
				new Label("Subset Posts (subsetPosts)"),
				lvSubsetPosts
		);

		lvAllPosts.setPrefHeight(220);
		lvSubsetPosts.setPrefHeight(220);

		HBox pane = new HBox(12, inputs, lists);
		pane.setPadding(new Insets(8));
		pane.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

		return pane;
	}

	private Pane buildReplyPane() {

		tfReplyId.setPromptText("ReplyId (for update/delete)");
		tfReplyPostId.setPromptText("PostId (reply belongs to this post)");
		tfReplyAuthor.setPromptText("Author username");
		taReplyBody.setPromptText("Reply body");
		taReplyBody.setPrefRowCount(4);

		tfReplySearch.setPromptText("Search keyword");

		Button btnCreate = new Button("Create Reply");
		btnCreate.setOnAction(e -> controller.handleCreateReply());

		Button btnUpdate = new Button("Update Reply");
		btnUpdate.setOnAction(e -> controller.handleUpdateReply());

		Button btnDelete = new Button("Delete Reply");
		btnDelete.setOnAction(e -> controller.handleDeleteReply());

		Button btnSearch = new Button("Search Replies");
		btnSearch.setOnAction(e -> controller.handleSearchReplies());

		Button btnFilterByPost = new Button("Filter by PostId");
		btnFilterByPost.setOnAction(e -> controller.handleFilterRepliesByPostId());

		Button btnClearSubset = new Button("Clear Reply Subset");
		btnClearSubset.setOnAction(e -> controller.handleClearReplySubset());

		VBox inputs = new VBox(6,
				new Label("REPLY INPUTS"),
				tfReplyId,
				tfReplyPostId,
				tfReplyAuthor,
				taReplyBody,
				new HBox(6, btnCreate, btnUpdate, btnDelete),
				new Separator(),
				new Label("REPLY SEARCH/FILTER (subsetReplies)"),
				tfReplySearch,
				new HBox(6, btnSearch, btnFilterByPost, btnClearSubset)
		);

		VBox lists = new VBox(6,
				new Label("All Replies (allReplies)"),
				lvAllReplies,
				new Label("Subset Replies (subsetReplies)"),
				lvSubsetReplies
		);

		lvAllReplies.setPrefHeight(220);
		lvSubsetReplies.setPrefHeight(220);

		HBox pane = new HBox(12, inputs, lists);
		pane.setPadding(new Insets(8));
		pane.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

		return pane;
	}

	// -------------------------
	// Getters for Controller
	// -------------------------

	public Integer getPostId() { return parseIntOrNull(tfPostId.getText()); }
	public String getPostAuthor() { return tfPostAuthor.getText(); }
	public String getPostThread() { return tfPostThread.getText(); }
	public String getPostTitle() { return tfPostTitle.getText(); }
	public String getPostBody() { return taPostBody.getText(); }
	public String getPostSearchKeyword() { return tfPostSearch.getText(); }
	public boolean getDeleteConfirm() { return cbConfirmDelete.isSelected(); }

	public Integer getReplyId() { return parseIntOrNull(tfReplyId.getText()); }
	public Integer getReplyPostId() { return parseIntOrNull(tfReplyPostId.getText()); }
	public String getReplyAuthor() { return tfReplyAuthor.getText(); }
	public String getReplyBody() { return taReplyBody.getText(); }
	public String getReplySearchKeyword() { return tfReplySearch.getText(); }

	private Integer parseIntOrNull(String s) {
		try {
			if (s == null) return null;
			String t = s.trim();
			if (t.isEmpty()) return null;
			return Integer.parseInt(t);
		} catch (Exception ex) {
			return null;
		}
	}

	// -------------------------
	// Setters for list updates
	// -------------------------

	public void setMessage(String msg) {
		lblMessage.setText(msg);
	}

	public void setAllPosts(ArrayList<Post> posts) {
		lvAllPosts.getItems().clear();
		for (Post p : posts) lvAllPosts.getItems().add(p.toString());
	}

	public void setSubsetPosts(ArrayList<Post> posts) {
		lvSubsetPosts.getItems().clear();
		for (Post p : posts) lvSubsetPosts.getItems().add(p.toString());
	}

	public void setAllReplies(ArrayList<Reply> replies) {
		lvAllReplies.getItems().clear();
		for (Reply r : replies) lvAllReplies.getItems().add(r.toString());
	}

	public void setSubsetReplies(ArrayList<Reply> replies) {
		lvSubsetReplies.getItems().clear();
		for (Reply r : replies) lvSubsetReplies.getItems().add(r.toString());
	}
}