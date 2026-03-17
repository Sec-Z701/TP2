package store;

import java.util.ArrayList;
import entityClasses.Reply;
import validation.ReplyValidator;

/*******
 * <p> Title: ReplyStore Class </p>
 *
 * <p> Description: Stores all Replies and a subset of Replies (filter/search results).
 * Replies are linked to Posts by postId. Provides CRUD operations in memory. </p>
 *
 */
public class ReplyStore {

	private ArrayList<Reply> allReplies = new ArrayList<>();
	private ArrayList<Reply> subsetReplies = new ArrayList<>();
	private int nextReplyId = 1;

	private PostStore postStore;  

	public ReplyStore(PostStore postStore) {
		this.postStore = postStore;
	}

	public ArrayList<Reply> getAllReplies() { return allReplies; }
	public ArrayList<Reply> getSubsetReplies() { return subsetReplies; }

	public Reply readReplyById(int replyId) {
		for (Reply r : allReplies)
			if (r.getReplyId() == replyId)
				return r;
		return null;
	}

	public String createReply(int postId, String author, String body) {

		if (!postStore.postExists(postId))
			return "Cannot reply: postId does not exist.";

		String err = ReplyValidator.validateForCreate(author, body);
		if (!err.isEmpty()) return err;

		Reply r = new Reply(nextReplyId++, postId, author.trim(), body.trim());
		allReplies.add(r);
		return "";
	}

	public ArrayList<Reply> getRepliesForPost(int postId) {
		ArrayList<Reply> results = new ArrayList<>();
		for (Reply r : allReplies)
			if (r.getPostId() == postId)
				results.add(r);
		return results;
	}

	public String updateReply(int replyId, String author, String body) {

		Reply r = readReplyById(replyId);
		if (r == null) return "Reply not found.";

		String err = ReplyValidator.validateForUpdate(author, body);
		if (!err.isEmpty()) return err;

		r.setAuthorUsername(author.trim());
		r.setBody(body.trim());
		r.touchUpdatedAt();

		return "";
	}

	public String deleteReply(int replyId) {

		Reply r = readReplyById(replyId);
		if (r == null) return "Reply not found.";

		allReplies.remove(r);
		subsetReplies.remove(r);
		return "";
	}

	public String searchRepliesByKeyword(String keyword) {

	    if (keyword == null || keyword.trim().isEmpty())
	        return "Search keyword is required.";

	    String k = keyword.trim().toLowerCase();
	    subsetReplies.clear();

	    for (Reply r : allReplies) {
	        String hay = r.getBody().toLowerCase();
	        if (hay.contains(k)) subsetReplies.add(r);
	    }

	    if (subsetReplies.isEmpty())
	        return "No matching replies found.";

	    return "";
	}

	public void filterRepliesByPostId(int postId) {
		subsetReplies.clear();
		for (Reply r : allReplies)
			if (r.getPostId() == postId)
				subsetReplies.add(r);
	}

	public void clearSubsetReplies() {
		subsetReplies.clear();
	}
}