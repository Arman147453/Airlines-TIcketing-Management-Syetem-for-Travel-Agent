package traveldestination.java;
import java.sql.Connection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AirlinesProject extends Application {

    private Button selectDepartureDateButton;
    private Button selectReturnDateButton;
    private LocalDate departureDate;
    private LocalDate returnDate;

    @Override
    public void start(Stage primaryStage) {
        // User Login Interface
        VBox loginLayout = createLoginLayout(primaryStage);

        // Scene for user login
        Scene loginScene = new Scene(loginLayout, 400, 238);

        // Set user login scene as the primary scene
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Travel Destination BD - User Login");
        primaryStage.show();
    }

    private VBox createLoginLayout(Stage primaryStage) {
        // Creating UI components for user login
        Label signInLabel = new Label("Sign in to Your Account");
        Label emailLabel = new Label("*Email or User Name:");
        TextField emailTextField = new TextField();
        Label passwordLabel = new Label("*Password:");
        PasswordField passwordField = new PasswordField();
        Button signInButton = new Button("Sign in");
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Your Password?");

        // Layout for user login
        GridPane loginGridPane = new GridPane();
        loginGridPane.setVgap(10);
        loginGridPane.setHgap(10);
        loginGridPane.addRow(0, signInLabel);
        loginGridPane.addRow(1, emailLabel, emailTextField);
        loginGridPane.addRow(2, passwordLabel, passwordField);
        loginGridPane.addRow(3, signInButton);
        loginGridPane.addRow(4, forgotPasswordLink);

        VBox.setMargin(loginGridPane, new Insets(20));

        // Button actions for user login
        signInButton.setOnAction(e -> {
            String email = emailTextField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Warning", "Please fill up all the fields.");
            } else if (isValidUser(email, password)) {
                // User authentication successful, proceed to main interface
                VBox mainLayout = createMainLayout(primaryStage);
                Scene mainScene = new Scene(mainLayout, 1000, 630);
                primaryStage.setScene(mainScene);
            } else {
                // User authentication failed, show error message
                showAlert("Error", "Incorrect Email or User Name or Password!! Please try again.");
            }
        });

        forgotPasswordLink.setOnAction(e -> {
            // Show contact information for password recovery
            showAlert("Forgot Password", "Please contact with our reservation team: 01967-686862");
        });

        // Main layout for user login
        VBox loginMainLayout = new VBox(22);
        loginMainLayout.setPadding(new Insets(20));
        loginMainLayout.getChildren().addAll(loginGridPane);

        return loginMainLayout;
    }

    private boolean isValidUser(String email, String password) {
        // Check if the email/user name and password match the correct credentials
        return email.equals("arman57468@gmail.com") && password.equals("123");
    }

    private VBox createMainLayout(Stage primaryStage) {
        // Creating UI components
        Label titleLabel = new Label("Welcome to Travel Destination BD!");
        Label passengerLabel = new Label("Please select flight details: ");
        Label flightTypeLabel = new Label("Flight Type:");
        ToggleGroup flightTypeToggleGroup = new ToggleGroup();
        RadioButton oneWayRadioButton = new RadioButton("One Way");
        oneWayRadioButton.setToggleGroup(flightTypeToggleGroup);
        RadioButton roundTripRadioButton = new RadioButton("Round Trip");
        roundTripRadioButton.setToggleGroup(flightTypeToggleGroup);
        RadioButton multiCityRadioButton = new RadioButton("Multi City");
        multiCityRadioButton.setToggleGroup(flightTypeToggleGroup);
        HBox flightTypeBox = new HBox(10, oneWayRadioButton, roundTripRadioButton, multiCityRadioButton);

        Label fromLabel = new Label("From:");
        ComboBox<String> fromComboBox = new ComboBox<>();
        fromComboBox.getItems().addAll("Dhaka (DAC)", "Saidpur (SPD)", "Sylhet (ZYL)", "Chittagong (CGP)", "Cox's Bazar (CXB)", "Jessore (JSR)", "Rajshahi (RJH)", "Barishal (BZL)");
        Label toLabel = new Label("To:");
        ComboBox<String> toComboBox = new ComboBox<>();
        toComboBox.getItems().addAll("Dhaka (DAC)", "Saidpur (SPD)", "Sylhet (ZYL)", "Chittagong (CGP)", "Cox's Bazar (CXB)", "Jessore (JSR)", "Rajshahi (RJH)", "Barishal (BZL)");

        selectDepartureDateButton = new Button("Select Departure Date");
        selectReturnDateButton = new Button("Select Return Date");
        selectReturnDateButton.setDisable(true);

        HBox dateBox = new HBox(10, selectDepartureDateButton, selectReturnDateButton);

        Label numberOfTravelersLabel = new Label("Number of Travelers:");
        Spinner<Integer> adultSpinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
        Spinner<Integer> childSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);
        Spinner<Integer> infantSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);
        HBox travelersBox = new HBox(10, new Label("Adults (12 years and above):"), adultSpinner, new Label("Children (2 years - under 12 years):"), childSpinner, new Label("Infants (below 2 years):"), infantSpinner);

        Label bookingClassLabel = new Label("Booking Class:");
        ComboBox<String> bookingClassComboBox = new ComboBox<>();
        bookingClassComboBox.getItems().addAll("Economy", "Premium Economy", "Business", "First Class");

        Button searchButton = new Button("Search");

        // Main layout
        VBox mainLayout = new VBox(22);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(titleLabel, passengerLabel, flightTypeLabel, flightTypeBox, fromLabel, fromComboBox, toLabel, toComboBox, dateBox, numberOfTravelersLabel, travelersBox, bookingClassLabel, bookingClassComboBox, searchButton);

        // Button actions
        flightTypeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == oneWayRadioButton) {
                selectReturnDateButton.setDisable(true);
            } else if (newToggle == roundTripRadioButton) {
                selectReturnDateButton.setDisable(false);
            }
        });

        selectDepartureDateButton.setOnAction(e -> selectDate("Departure"));
        selectReturnDateButton.setOnAction(e -> selectDate("Return"));

        searchButton.setOnAction(e -> {
            // Perform search and display flight details
            if (validateFields() && validateAirportSelection(fromComboBox.getValue(), toComboBox.getValue())) {
                displayFlightDetails(primaryStage);
            } else {
                showAlert("Warning", "Please fill up all the fields correctly.");
            }
        });

        return mainLayout;
    }

    private boolean validateFields() {
        // Validate if all necessary fields are filled
        boolean isValid = true;
        if (selectDepartureDateButton.getText().equals("Select Departure Date") || (selectReturnDateButton.getText().equals("Select Return Date") && !selectReturnDateButton.isDisabled())) {
            isValid = false;
        }
        return isValid;
    }

    private boolean validateAirportSelection(String from, String to) {
        // Validate if From and To airports are not the same
        if (from.equals(to)) {
            showAlert("Caution", "From & To airports can't be same!");
            return false;
        }
        return true;
    }

    private void selectDate(String type) {
        // Add action for selecting date
        DatePicker datePicker = new DatePicker();
        datePicker.setShowWeekNumbers(true);
        VBox vbox = new VBox(datePicker);
        vbox.setPadding(new Insets(55));
        Stage stage = new Stage();
        stage.setScene(new Scene(vbox));
        stage.show();

        // Set action for selecting date
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (type.equals("Departure")) {
                departureDate = selectedDate;
                selectDepartureDateButton.setText("Departure Date: " + selectedDate);
            } else if (type.equals("Return")) {
                returnDate = selectedDate;
                selectReturnDateButton.setText("Return Date: " + selectedDate);
            }
            stage.close(); // Close the date picker window after selecting date
        });
    }

    private void displayFlightDetails(Stage primaryStage) {
        // Implement flight search and display flight details here
        // Dummy data for demonstration
        String flightDetails = "Flight Details:\n\n" +
                "Biman Bangladesh Airlines BG | 435\n" +
                "Depart: 11:30\n" +
                "Arrive: 12:45\n" +
                "Duration: 1 hour 15 minutes (Non-stop)\n" +
                "Aircraft : DHC8 Dash 8\n" +
                "Available Seats : 9\n" +
                "Price: BDT 3,000";

        // Display flight details
        TextArea flightDetailsTextArea = new TextArea(flightDetails);
        flightDetailsTextArea.setEditable(false);
        Button bookFlightButton = new Button("Book This Flight");

        // Action for booking flight
        bookFlightButton.setOnAction(e -> showPassengerInformation(primaryStage));

        VBox flightDetailsLayout = new VBox(10, flightDetailsTextArea, bookFlightButton);
        flightDetailsLayout.setPadding(new Insets(20));

        Scene flightDetailsScene = new Scene(flightDetailsLayout, 600, 300);
        primaryStage.setScene(flightDetailsScene);
    }

    private void showPassengerInformation(Stage primaryStage) {
        // Passenger information interface
        Label titleLabel = new Label("Please enter traveller Information");
        Label titleLabel2 = new Label("Title:");
        ComboBox<String> titleComboBox = new ComboBox<>();
        titleComboBox.getItems().addAll("Mr.", "Mrs.", "Miss");
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label emailLabel = new Label("Email Address:");
        TextField emailTextField = new TextField();
        Label contactLabel = new Label("Contact Number:");
        TextField contactTextField = new TextField();
        Button submitButton = new Button("Submit");

        VBox passengerInfoLayout = new VBox(10, titleLabel, titleLabel2, titleComboBox, nameLabel, nameTextField, emailLabel, emailTextField, contactLabel, contactTextField, submitButton);
        passengerInfoLayout.setPadding(new Insets(20));
        Scene passengerInfoScene = new Scene(passengerInfoLayout, 400, 350);

        // Action for submit button
        submitButton.setOnAction(e -> {
            // Validate email address
            if (!isValidEmail(emailTextField.getText())) {
                showAlert("Invalid Email!", "Please enter a valid email address.");
                return;
            }

            // Validate contact number
            if (!isNumeric(contactTextField.getText())) {
                showAlert("Invalid Contact Number!", "Please enter a valid numeric contact number.");
                return;
            }

            // Show confirmation message
            showAlert("Booking Confirmation", "Thanks for your interest and your valuable query. Our reservation team will contact with you soon.");
        });

        primaryStage.setScene(passengerInfoScene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        // Basic email validation using regular expression
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isNumeric(String str) {
        // Check if a string is numeric
        return str.matches("\\d+");
    }

    public static void main(String[] args) {
        launch(args);
    }
}