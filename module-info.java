module com.example.traveldestination {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Ensure SQL is included

    opens traveldestination.java to javafx.graphics, javafx.fxml;
    exports traveldestination.java;
}
