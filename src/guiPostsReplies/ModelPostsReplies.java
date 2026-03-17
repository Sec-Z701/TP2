package guiPostsReplies;

import java.util.ArrayList;

import entityClasses.Post;
import entityClasses.Reply;
import store.PostStore;
import store.ReplyStore;

/*******
 * <p> Title: ModelPostsReplies Class </p>
 *
 * <p> Description: Model for the HW2 Posts/Replies GUI. Holds PostStore and ReplyStore
 * and provides methods for CRUD + subset operations. </p>
 *
 */
public class ModelPostsReplies {

	private PostStore postStore;
	private ReplyStore replyStore;

	public ModelPostsReplies() {
		postStore = new PostStore();
		replyStore = new ReplyStore(postStore);
	}

	// -----------------------
	// POST operations
	// -----------------------

	public String createPost(String author, String thread, String title, String body) {
		return postStore.createPost(author, thread, title, body);
	}

	public String updatePost(int postId, String author, String thread, String title, String body) {
		return postStore.updatePost(postId, author, thread, title, body);
	}

	public String deletePost(int postId, boolean confirmDelete) {
		return postStore.deletePost(postId, confirmDelete);
	}

	public String searchPosts(String keyword) {
		return postStore.searchPostsByKeyword(keyword);
	}

	public void clearPostSubset() {
		postStore.clearSubsetPosts();
	}

	public ArrayList<Post> getAllPosts() {
		return postStore.getAllPosts();
	}

	public ArrayList<Post> getSubsetPosts() {
		return postStore.getSubsetPosts();
	}

	// -----------------------
	// REPLY operations
	// -----------------------

	public String createReply(int postId, String author, String body) {
		return replyStore.createReply(postId, author, body);
	}

	public String updateReply(int replyId, String author, String body) {
		return replyStore.updateReply(replyId, author, body);
	}

	public String deleteReply(int replyId) {
		return replyStore.deleteReply(replyId);
	}

	public String searchReplies(String keyword) {
		return replyStore.searchRepliesByKeyword(keyword);
	}

	public void filterRepliesByPostId(int postId) {
		replyStore.filterRepliesByPostId(postId);
	}

	public void clearReplySubset() {
		replyStore.clearSubsetReplies();
	}

	public ArrayList<Reply> getAllReplies() {
		return replyStore.getAllReplies();
	}

	public ArrayList<Reply> getSubsetReplies() {
		return replyStore.getSubsetReplies();
	}

	public ArrayList<Reply> getRepliesForPost(int postId) {
		return replyStore.getRepliesForPost(postId);
	}
}