package store;

import java.util.ArrayList;
import entityClasses.Post;
import validation.PostValidator;

/*******
 * <p> Title: PostStore Class </p>
 *
 * <p> Description: Stores all Posts and a subset of Posts (search results). Provides
 * CRUD operations and keyword search in memory. </p>
 *
 */
public class PostStore {

	private ArrayList<Post> allPosts = new ArrayList<>();
	private ArrayList<Post> subsetPosts = new ArrayList<>();
	private int nextPostId = 1;

	public ArrayList<Post> getAllPosts() { return allPosts; }
	public ArrayList<Post> getSubsetPosts() { return subsetPosts; }

	public boolean postExists(int postId) {
		return readPostById(postId) != null;
	}

	public Post readPostById(int postId) {
		for (Post p : allPosts)
			if (p.getPostId() == postId)
				return p;
		return null;
	}

	public String createPost(String author, String thread, String title, String body) {

		String err = PostValidator.validateForCreate(author, thread, title, body);
		if (!err.isEmpty()) return err;

		String normalizedThread = PostValidator.normalizeThread(thread);

		Post p = new Post(nextPostId++, author.trim(), normalizedThread, title.trim(), body.trim());
		allPosts.add(p);
		return "";
	}

	public String updatePost(int postId, String author, String thread, String title, String body) {

		Post p = readPostById(postId);
		if (p == null) return "Post not found.";

		String err = PostValidator.validateForUpdate(author, thread, title, body);
		if (!err.isEmpty()) return err;

		p.setAuthorUsername(author.trim());
		p.setThread(PostValidator.normalizeThread(thread));
		p.setTitle(title.trim());
		p.setBody(body.trim());
		p.touchUpdatedAt();

		return "";
	}

	public String deletePost(int postId, boolean confirmDelete) {

		Post p = readPostById(postId);
		if (p == null) return "Post not found.";

		if (!confirmDelete) return "Delete cancelled.";

		allPosts.remove(p);

		subsetPosts.remove(p);

		return "";
	}

	public String searchPostsByKeyword(String keyword) {

	    if (keyword == null || keyword.trim().isEmpty())
	        return "Search keyword is required.";

	    String k = keyword.trim().toLowerCase();
	    subsetPosts.clear();

	    for (Post p : allPosts) {
	        String hay = (p.getTitle() + " " + p.getBody() + " " + p.getThread()).toLowerCase();
	        if (hay.contains(k)) subsetPosts.add(p);
	    }

	    if (subsetPosts.isEmpty())
	        return "No matching posts found.";

	    return "";
	}

	public void clearSubsetPosts() {
		subsetPosts.clear();
	}
}