// Import necessary JavaFX classes and other libraries
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// Import libraries for JSON parsing and HTTP requests
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Main class extending JavaFX Application
public class WeatherApp extends Application {

    // API key and URL template for fetching weather data
    private static final String API_KEY = "your_api"; // Replace with your OpenWeatherMap API key
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=" + API_KEY;

    // UI components and variables for user input and displaying weather data
    private TextField locationInput; // Input field for city name
    private Label temperatureLabel;  // Label to display temperature
    private Label humidityLabel;     // Label to display humidity
    private Label windSpeedLabel;    // Label to display wind speed
    private Label conditionLabel;    // Label to display weather condition
    private ImageView weatherIcon;   // Icon to represent weather condition
    private ListView<String> historyListView; // List view to show search history
    private List<String> history;    // List to store history
    private String unit = "metric";  // Default unit for temperature (Celsius)

    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather Information App"); // Set window title

        // Create UI components
        locationInput = new TextField();
        locationInput.setPromptText("Enter city name..."); // Placeholder text

        Button fetchWeatherButton = new Button("Get Weather");
        fetchWeatherButton.setOnAction(e -> fetchWeatherData()); // Fetch data when clicked

        Button toggleUnitButton = new Button("Switch to Fahrenheit");
        toggleUnitButton.setOnAction(e -> toggleUnit(toggleUnitButton)); // Toggle unit when clicked

        // Labels to display weather details
        temperatureLabel = new Label("Temperature: ");
        humidityLabel = new Label("Humidity: ");
        windSpeedLabel = new Label("Wind Speed: ");
        conditionLabel = new Label("Condition: ");

        // ImageView for weather icon
        weatherIcon = new ImageView();
        weatherIcon.setFitWidth(100);
        weatherIcon.setFitHeight(100);

        // Initialize history list and ListView
        history = new ArrayList<>();
        historyListView = new ListView<>();
        historyListView.setPrefHeight(150); // Set height of history list

        // Layout for weather details
        VBox weatherDetails = new VBox(10, temperatureLabel, humidityLabel, windSpeedLabel, conditionLabel, weatherIcon);
        weatherDetails.setPadding(new Insets(10));

        // Main layout for the app
        VBox layout = new VBox(15, locationInput, fetchWeatherButton, toggleUnitButton, weatherDetails, new Label("History:"), historyListView);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: lightblue;"); // Background color

        // Set up and display the scene
        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchWeatherData() {
        // Get user input for location
        String location = locationInput.getText().trim();
        if (location.isEmpty()) {
            showAlert("Error", "Please enter a location.", Alert.AlertType.ERROR); // Show error if input is empty
            return;
        }

        try {
            // Construct API URL with user input and selected unit
            String urlString = String.format(API_URL, location, unit);
            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // Set HTTP method

            if (connection.getResponseCode() == 200) { // Check if response is successful
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject(); // Parse JSON response

                updateWeatherDetails(response); // Update UI with weather data
                updateHistory(location);       // Update search history
            } else {
                showAlert("Error", "Failed to fetch weather data. Please check the location.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR); // Show error message
        }
    }

    private void updateWeatherDetails(JsonObject response) {
        // Extract weather data from JSON response
        JsonObject main = response.getAsJsonObject("main");
        JsonObject wind = response.getAsJsonObject("wind");
        String condition = response.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
        String iconCode = response.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        double temperature = main.get("temp").getAsDouble(); // Temperature
        int humidity = main.get("humidity").getAsInt();      // Humidity
        double windSpeed = wind.get("speed").getAsDouble();  // Wind speed

        // Update UI labels with fetched data
        temperatureLabel.setText("Temperature: " + temperature + (unit.equals("metric") ? " °C" : " °F"));
        humidityLabel.setText("Humidity: " + humidity + "%");
        windSpeedLabel.setText("Wind Speed: " + windSpeed + (unit.equals("metric") ? " m/s" : " mph"));
        conditionLabel.setText("Condition: " + condition);

        // Fetch and display weather icon
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        weatherIcon.setImage(new Image(iconUrl));
    }

    private void updateHistory(String location) {
        // Add location and timestamp to history
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        String entry = "[" + timestamp + "] Location: " + location + " (" + (unit.equals("metric") ? "Celsius" : "Fahrenheit") + ")";
        history.add(entry); // Add to history list
        historyListView.getItems().setAll(history); // Update ListView
    }

    private void toggleUnit(Button toggleUnitButton) {
        // Switch between metric and imperial units
        if (unit.equals("metric")) {
            unit = "imperial";
            toggleUnitButton.setText("Switch to Celsius");
        } else {
            unit = "metric";
            toggleUnitButton.setText("Switch to Fahrenheit");
        }
        fetchWeatherData(); // Refresh weather data with the new unit
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        // Display an alert dialog to the user
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
