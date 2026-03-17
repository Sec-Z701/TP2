package validation;

/*******
 * <p> Title: ReplyValidator Class </p>
 *
 * <p> Description: Validates Reply inputs for CRUD operations. Returns an empty string
 * when valid; otherwise returns a helpful error message. </p>
 *
 */
public class ReplyValidator {

	public static String validateForCreate(String author, String body) {

		if (author == null || author.trim().isEmpty())
			return "Author username is required.";

		if (body == null || body.trim().isEmpty())
			return "Body is required.";

		String b = body.trim();
		if (b.length() < 1 || b.length() > 2000)
			return "Body must be between 1 and 2000 characters.";

		return "";
	}

	public static String validateForUpdate(String author, String body) {
		return validateForCreate(author, body);
	}
}