module FoundationsF25 {
    requires javafx.controls;
    requires java.sql;
    requires java.desktop;

    opens applicationMain to javafx.graphics, javafx.fxml;

    opens hw2app to javafx.graphics;
    opens guiPostsReplies to javafx.graphics;
}