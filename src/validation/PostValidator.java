package validation;

/*******
 * <p> Title: PostValidator Class </p>
 *
 * <p> Description: Validates Post inputs for CRUD operations. Returns an empty string
 * when valid; otherwise returns a helpful error message. </p>
 *
 */
public class PostValidator {

	public static final String DEFAULT_THREAD = "General";

	public static String validateForCreate(String author, String thread, String title, String body) {

		if (author == null || author.trim().isEmpty())
			return "Author username is required.";

		if (title == null || title.trim().isEmpty())
			return "Title is required.";

		String t = title.trim();
		if (t.length() < 5 || t.length() > 80)
			return "Title must be between 5 and 80 characters.";

		if (body == null || body.trim().isEmpty())
			return "Body is required.";

		String b = body.trim();
		if (b.length() < 1 || b.length() > 2000)
			return "Body must be between 1 and 2000 characters.";

		// thread can be blank; store will default it
		return "";
	}

	public static String normalizeThread(String thread) {
		if (thread == null || thread.trim().isEmpty())
			return DEFAULT_THREAD;
		return thread.trim();
	}

	public static String validateForUpdate(String author, String thread, String title, String body) {
		return validateForCreate(author, thread, title, body);
	}
}