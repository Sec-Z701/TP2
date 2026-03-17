package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Reply Class </p>
 *
 * <p> Description: Represents a reply to a Post in the HW2 in-memory discussion system.
 * A Reply is linked to a Post by postId and supports CRUD and searching. </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 *
 *
 */
public class Reply {

	private int replyId;
	private int postId;
	private String authorUsername;
	private String body;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Reply() { }

	public Reply(int replyId, int postId, String authorUsername, String body) {
		this.replyId = replyId;
		this.postId = postId;
		this.authorUsername = authorUsername;
		this.body = body;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	public int getReplyId() { return replyId; }

	public int getPostId() { return postId; }
	public void setPostId(int postId) { this.postId = postId; }

	public String getAuthorUsername() { return authorUsername; }
	public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void touchUpdatedAt() { this.updatedAt = LocalDateTime.now(); }

	@Override
	public String toString() {
		return "ReplyId=" + replyId + ", postId=" + postId + ", author=" + authorUsername;
	}
}