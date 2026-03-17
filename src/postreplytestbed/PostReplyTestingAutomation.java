package postreplytestbed;

import store.PostStore;
import store.ReplyStore;

/*******
 * <p> Title: PostReplyTestingAutomation Class </p>
 *
 * <p> Description: Runs automated tests for HW2 Post/Reply CRUD + validation.
 * Prints pass/fail results and a final summary. </p>
 *
 *
 */
public class PostReplyTestingAutomation {

	// Counters
	private static int testsRun = 0;
	private static int testsPassed = 0;

	// Shared stores for tests
	private static PostStore postStore;
	private static ReplyStore replyStore;

	/*****
	 * <p> Method: main </p>
	 *
	 * <p> Description: Entry point. Runs all tests automatically. </p>
	 */
	public static void main(String[] args) {

		System.out.println("========================================");
		System.out.println(" HW2 Post/Reply Testing Automation");
		System.out.println("========================================\n");

		setUpFreshStores();

		// -------------------------
		// POST TESTS
		// -------------------------
		TC_P1_01_CreatePost_Positive();
		TC_P1_02_CreatePost_EmptyTitle();
		TC_P1_03_CreatePost_TitleTooShort();
		TC_P1_04_CreatePost_EmptyBody();
		TC_P1_05_CreatePost_EmptyAuthor();

		TC_P2_01_ReadAllPosts_Positive();
		TC_P2_02_ReadAllPosts_EmptyList();

		TC_P3_01_UpdatePost_Positive();
		TC_P3_02_UpdatePost_InvalidTitle();
		TC_P3_03_UpdatePost_PostNotFound();

		TC_P4_01_DeletePost_PositiveConfirmed();
		TC_P4_02_DeletePost_Cancelled();
		TC_P4_03_DeletePost_PostNotFound();

		TC_P5_01_SearchPosts_Positive();
		TC_P5_02_SearchPosts_EmptyKeyword();
		TC_P5_03_SearchPosts_NoMatches();

		TC_P6_01_ClearPostSubset();

		// -------------------------
		// REPLY TESTS
		// -------------------------
		TC_R1_01_CreateReply_Positive();
		TC_R1_02_CreateReply_PostDoesNotExist();
		TC_R1_03_CreateReply_EmptyBody();
		TC_R1_04_CreateReply_EmptyAuthor();

		TC_R2_01_ReadReplies_Positive();
		TC_R2_02_ReadReplies_EmptyList();

		TC_R3_01_UpdateReply_Positive();
		TC_R3_02_UpdateReply_InvalidBody();
		TC_R3_03_UpdateReply_ReplyNotFound();

		TC_R4_01_DeleteReply_Positive();
		TC_R4_02_DeleteReply_ReplyNotFound();

		TC_R5_01_SearchReplies_Positive();
		TC_R5_02_FilterReplies_ByPostId();
		TC_R5_03_SearchReplies_NoMatches();
		TC_R5_04_ClearReplySubset();

		// -------------------------
		// SUMMARY
		// -------------------------
		System.out.println("\n========================================");
		System.out.println(" TEST SUMMARY");
		System.out.println("========================================");
		System.out.println("Tests Run:    " + testsRun);
		System.out.println("Tests Passed: " + testsPassed);
		System.out.println("Tests Failed: " + (testsRun - testsPassed));
		System.out.println("========================================");
	}

	// ------------------------------------------------------------
	// Setup Helpers
	// ------------------------------------------------------------

	private static void setUpFreshStores() {
		postStore = new PostStore();
		replyStore = new ReplyStore(postStore);
	}


	private static void seedBasicPosts() {
		postStore.createPost("alex", "", "Exam 1 review", "When is the midterm?");
		postStore.createPost("bea", "General", "Homework question", "I am stuck on task 2.");
	}

	
	private static void seedBasicReplies() {
		replyStore.createReply(1, "bea", "Thanks! The midterm is next week.");
		replyStore.createReply(1, "cody", "I think it is on Thursday.");
		replyStore.createReply(2, "alex", "Try splitting the user stories into CRUD stories.");
	}

	// ------------------------------------------------------------
	// Assertion Helpers (simple and readable)
	// ------------------------------------------------------------

	private static void assertTrue(String tcId, boolean condition, String passMsg, String failMsg) {
		testsRun++;
		if (condition) {
			testsPassed++;
			System.out.println("[PASS] " + tcId + " - " + passMsg);
		} else {
			System.out.println("[FAIL] " + tcId + " - " + failMsg);
		}
	}

	private static void assertEquals(String tcId, String expected, String actual) {
		testsRun++;
		if (expected.equals(actual)) {
			testsPassed++;
			System.out.println("[PASS] " + tcId + " - Expected == Actual == \"" + expected + "\"");
		} else {
			System.out.println("[FAIL] " + tcId + " - Expected \"" + expected + "\" but got \"" + actual + "\"");
		}
	}

	private static void assertIntEquals(String tcId, int expected, int actual, String label) {
		testsRun++;
		if (expected == actual) {
			testsPassed++;
			System.out.println("[PASS] " + tcId + " - " + label + " == " + expected);
		} else {
			System.out.println("[FAIL] " + tcId + " - " + label + " expected " + expected + " but got " + actual);
		}
	}

	// ------------------------------------------------------------
	// POST TESTS
	// ------------------------------------------------------------

	private static void TC_P1_01_CreatePost_Positive() {
		setUpFreshStores();
		String err = postStore.createPost("alex", "", "Exam 1 review", "When is the midterm?");
		assertEquals("TC-P1-01", "", err);
		assertIntEquals("TC-P1-01a", 1, postStore.getAllPosts().size(), "allPosts size");
		assertTrue("TC-P1-01b",
				"General".equals(postStore.getAllPosts().get(0).getThread()),
				"Thread defaulted to General",
				"Thread did not default to General");
	}

	private static void TC_P1_02_CreatePost_EmptyTitle() {
		setUpFreshStores();
		String err = postStore.createPost("alex", "General", "", "Valid body");
		assertEquals("TC-P1-02", "Title is required.", err);
	}

	private static void TC_P1_03_CreatePost_TitleTooShort() {
		setUpFreshStores();
		String err = postStore.createPost("alex", "General", "Hey", "Valid body");
		assertEquals("TC-P1-03", "Title must be between 5 and 80 characters.", err);
	}

	private static void TC_P1_04_CreatePost_EmptyBody() {
		setUpFreshStores();
		String err = postStore.createPost("alex", "General", "Valid title", "");
		assertEquals("TC-P1-04", "Body is required.", err);
	}

	private static void TC_P1_05_CreatePost_EmptyAuthor() {
		setUpFreshStores();
		String err = postStore.createPost("", "General", "Valid title", "Valid body");
		assertEquals("TC-P1-05", "Author username is required.", err);
	}

	private static void TC_P2_01_ReadAllPosts_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		assertIntEquals("TC-P2-01", 2, postStore.getAllPosts().size(), "allPosts size");
	}

	private static void TC_P2_02_ReadAllPosts_EmptyList() {
		setUpFreshStores();
		assertIntEquals("TC-P2-02", 0, postStore.getAllPosts().size(), "allPosts size");
	}

	private static void TC_P3_01_UpdatePost_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.updatePost(1, "alex", "General", "Updated title", "Updated body");
		assertEquals("TC-P3-01", "", err);
		assertTrue("TC-P3-01a",
				"Updated title".equals(postStore.readPostById(1).getTitle()),
				"Title updated",
				"Title not updated");
	}

	private static void TC_P3_02_UpdatePost_InvalidTitle() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.updatePost(1, "alex", "General", "", "Updated body");
		assertEquals("TC-P3-02", "Title is required.", err);
	}

	private static void TC_P3_03_UpdatePost_PostNotFound() {
		setUpFreshStores();
		String err = postStore.updatePost(999, "alex", "General", "Valid title", "Valid body");
		assertEquals("TC-P3-03", "Post not found.", err);
	}

	private static void TC_P4_01_DeletePost_PositiveConfirmed() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.deletePost(1, true);
		assertEquals("TC-P4-01", "", err);
		assertIntEquals("TC-P4-01a", 1, postStore.getAllPosts().size(), "allPosts size");
	}

	private static void TC_P4_02_DeletePost_Cancelled() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.deletePost(1, false);
		assertEquals("TC-P4-02", "Delete cancelled.", err);
		assertIntEquals("TC-P4-02a", 2, postStore.getAllPosts().size(), "allPosts size");
	}

	private static void TC_P4_03_DeletePost_PostNotFound() {
		setUpFreshStores();
		String err = postStore.deletePost(999, true);
		assertEquals("TC-P4-03", "Post not found.", err);
	}

	private static void TC_P5_01_SearchPosts_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.searchPostsByKeyword("midterm");
		assertEquals("TC-P5-01", "", err);
		assertIntEquals("TC-P5-01a", 1, postStore.getSubsetPosts().size(), "subsetPosts size");
	}

	private static void TC_P5_02_SearchPosts_EmptyKeyword() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.searchPostsByKeyword("");
		assertEquals("TC-P5-02", "Search keyword is required.", err);
	}

	private static void TC_P5_03_SearchPosts_NoMatches() {
		setUpFreshStores();
		seedBasicPosts();
		String err = postStore.searchPostsByKeyword("zzzz");
		assertEquals("TC-P5-03", "No matching posts found.", err);
		assertIntEquals("TC-P5-03a", 0, postStore.getSubsetPosts().size(), "subsetPosts size");
	}

	private static void TC_P6_01_ClearPostSubset() {
		setUpFreshStores();
		seedBasicPosts();
		postStore.searchPostsByKeyword("midterm");
		postStore.clearSubsetPosts();
		assertIntEquals("TC-P6-01", 0, postStore.getSubsetPosts().size(), "subsetPosts size");
	}

	// ------------------------------------------------------------
	// REPLY TESTS
	// ------------------------------------------------------------

	private static void TC_R1_01_CreateReply_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		String err = replyStore.createReply(1, "alex", "Thanks for the info!");
		assertEquals("TC-R1-01", "", err);
		assertIntEquals("TC-R1-01a", 1, replyStore.getAllReplies().size(), "allReplies size");
	}

	private static void TC_R1_02_CreateReply_PostDoesNotExist() {
		setUpFreshStores();
		String err = replyStore.createReply(999, "alex", "Valid body");
		assertEquals("TC-R1-02", "Cannot reply: postId does not exist.", err);
	}

	private static void TC_R1_03_CreateReply_EmptyBody() {
		setUpFreshStores();
		seedBasicPosts();
		String err = replyStore.createReply(1, "alex", "");
		assertEquals("TC-R1-03", "Body is required.", err);
	}

	private static void TC_R1_04_CreateReply_EmptyAuthor() {
		setUpFreshStores();
		seedBasicPosts();
		String err = replyStore.createReply(1, "", "Valid body");
		assertEquals("TC-R1-04", "Author username is required.", err);
	}

	private static void TC_R2_01_ReadReplies_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		seedBasicReplies();
		assertIntEquals("TC-R2-01", 2, replyStore.getRepliesForPost(1).size(), "replies for postId=1");
	}

	private static void TC_R2_02_ReadReplies_EmptyList() {
		setUpFreshStores();
		seedBasicPosts();
		assertIntEquals("TC-R2-02", 0, replyStore.getRepliesForPost(1).size(), "replies for postId=1");
	}

	private static void TC_R3_01_UpdateReply_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		replyStore.createReply(1, "alex", "Original");
		String err = replyStore.updateReply(1, "alex", "Updated");
		assertEquals("TC-R3-01", "", err);
	}

	private static void TC_R3_02_UpdateReply_InvalidBody() {
		setUpFreshStores();
		seedBasicPosts();
		replyStore.createReply(1, "alex", "Original");
		String err = replyStore.updateReply(1, "alex", "");
		assertEquals("TC-R3-02", "Body is required.", err);
	}

	private static void TC_R3_03_UpdateReply_ReplyNotFound() {
		setUpFreshStores();
		String err = replyStore.updateReply(999, "alex", "Valid body");
		assertEquals("TC-R3-03", "Reply not found.", err);
	}

	private static void TC_R4_01_DeleteReply_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		replyStore.createReply(1, "alex", "To be deleted");
		String err = replyStore.deleteReply(1);
		assertEquals("TC-R4-01", "", err);
		assertIntEquals("TC-R4-01a", 0, replyStore.getAllReplies().size(), "allReplies size");
	}

	private static void TC_R4_02_DeleteReply_ReplyNotFound() {
		setUpFreshStores();
		String err = replyStore.deleteReply(999);
		assertEquals("TC-R4-02", "Reply not found.", err);
	}

	private static void TC_R5_01_SearchReplies_Positive() {
		setUpFreshStores();
		seedBasicPosts();
		seedBasicReplies();
		String err = replyStore.searchRepliesByKeyword("thanks");
		assertEquals("TC-R5-01", "", err);
		assertTrue("TC-R5-01a",
				replyStore.getSubsetReplies().size() >= 1,
				"subsetReplies has matches",
				"subsetReplies has no matches");
	}

	private static void TC_R5_02_FilterReplies_ByPostId() {
		setUpFreshStores();
		seedBasicPosts();
		seedBasicReplies();
		replyStore.filterRepliesByPostId(1);
		assertIntEquals("TC-R5-02", 2, replyStore.getSubsetReplies().size(), "subsetReplies size");
	}

	private static void TC_R5_03_SearchReplies_NoMatches() {
		setUpFreshStores();
		seedBasicPosts();
		seedBasicReplies();
		String err = replyStore.searchRepliesByKeyword("zzzz");
		assertEquals("TC-R5-03", "No matching replies found.", err);
		assertIntEquals("TC-R5-03a", 0, replyStore.getSubsetReplies().size(), "subsetReplies size");
	}

	private static void TC_R5_04_ClearReplySubset() {
		setUpFreshStores();
		seedBasicPosts();
		seedBasicReplies();
		replyStore.searchRepliesByKeyword("thanks");
		replyStore.clearSubsetReplies();
		assertIntEquals("TC-R5-04", 0, replyStore.getSubsetReplies().size(), "subsetReplies size");
	}
}