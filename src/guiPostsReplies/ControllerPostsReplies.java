package guiPostsReplies;

/*******
 * <p> Title: ControllerPostsReplies Class </p>
 *
 * <p> Description: Controller for the HW2 Posts/Replies GUI. Reads values from View,
 * calls Model, and updates the View lists and message label. </p>
 *
 */
public class ControllerPostsReplies {

	private ModelPostsReplies model;
	private ViewPostsReplies view;

	public ControllerPostsReplies(ModelPostsReplies model, ViewPostsReplies view) {
		this.model = model;
		this.view = view;
	}

	// -----------------------
	// POST handlers
	// -----------------------

	public void handleCreatePost() {
		String err = model.createPost(
				view.getPostAuthor(),
				view.getPostThread(),
				view.getPostTitle(),
				view.getPostBody()
		);

		if (err.isEmpty()) view.setMessage("Post created.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleUpdatePost() {
		Integer postId = view.getPostId();
		if (postId == null) {
			view.setMessage("PostId must be a valid integer.");
			return;
		}

		String err = model.updatePost(
				postId,
				view.getPostAuthor(),
				view.getPostThread(),
				view.getPostTitle(),
				view.getPostBody()
		);

		if (err.isEmpty()) view.setMessage("Post updated.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleDeletePost() {
		Integer postId = view.getPostId();
		if (postId == null) {
			view.setMessage("PostId must be a valid integer.");
			return;
		}

		boolean confirm = view.getDeleteConfirm();

		String err = model.deletePost(postId, confirm);
		if (err.isEmpty()) view.setMessage("Post deleted.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleSearchPosts() {
		String err = model.searchPosts(view.getPostSearchKeyword());
		if (err.isEmpty()) {
			view.setMessage("Post search completed.");
		} else {
			view.setMessage(err);
		}
		refreshLists();
	}

	public void handleClearPostSubset() {
		model.clearPostSubset();
		view.setMessage("Post subset cleared.");
		refreshLists();
	}

	// -----------------------
	// REPLY handlers
	// -----------------------

	public void handleCreateReply() {
		Integer postId = view.getReplyPostId();
		if (postId == null) {
			view.setMessage("Reply postId must be a valid integer.");
			return;
		}

		String err = model.createReply(postId, view.getReplyAuthor(), view.getReplyBody());
		if (err.isEmpty()) view.setMessage("Reply created.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleUpdateReply() {
		Integer replyId = view.getReplyId();
		if (replyId == null) {
			view.setMessage("ReplyId must be a valid integer.");
			return;
		}

		String err = model.updateReply(replyId, view.getReplyAuthor(), view.getReplyBody());
		if (err.isEmpty()) view.setMessage("Reply updated.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleDeleteReply() {
		Integer replyId = view.getReplyId();
		if (replyId == null) {
			view.setMessage("ReplyId must be a valid integer.");
			return;
		}

		String err = model.deleteReply(replyId);
		if (err.isEmpty()) view.setMessage("Reply deleted.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleSearchReplies() {
		String err = model.searchReplies(view.getReplySearchKeyword());
		if (err.isEmpty()) view.setMessage("Reply search completed.");
		else view.setMessage(err);

		refreshLists();
	}

	public void handleFilterRepliesByPostId() {
		Integer postId = view.getReplyPostId();
		if (postId == null) {
			view.setMessage("Reply postId must be a valid integer.");
			return;
		}

		model.filterRepliesByPostId(postId);
		view.setMessage("Replies filtered by postId.");
		refreshLists();
	}

	public void handleClearReplySubset() {
		model.clearReplySubset();
		view.setMessage("Reply subset cleared.");
		refreshLists();
	}

	// -----------------------
	// Refresh View Lists
	// -----------------------

	public void refreshLists() {
		view.setAllPosts(model.getAllPosts());
		view.setSubsetPosts(model.getSubsetPosts());

		view.setAllReplies(model.getAllReplies());
		view.setSubsetReplies(model.getSubsetReplies());
	}
}