package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Post Class </p>
 *
 * <p> Description: Represents a discussion post in the HW2 in-memory discussion system.
 * A Post includes basic information needed to support CRUD operations and searching. </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * 
 *
 */
public class Post {

	private int postId;
	private String authorUsername;
	private String thread;
	private String title;
	private String body;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	
	public Post() { }

	public Post(int postId, String authorUsername, String thread, String title, String body) {
		this.postId = postId;
		this.authorUsername = authorUsername;
		this.thread = thread;
		this.title = title;
		this.body = body;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	// Getters / Setters

	public int getPostId() { return postId; }

	public String getAuthorUsername() { return authorUsername; }
	public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

	public String getThread() { return thread; }
	public void setThread(String thread) { this.thread = thread; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void touchUpdatedAt() { this.updatedAt = LocalDateTime.now(); }

	@Override
	public String toString() {
		return "PostId=" + postId + ", thread=" + thread + ", author=" + authorUsername + ", title=" + title;
	}
}