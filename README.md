# Weather Information App

## App Description
The Weather Information App is a Java-based desktop application that provides real-time weather updates for user-specified locations. With a user-friendly graphical interface built using JavaFX, the app allows users to:

- View current weather details, including temperature, humidity, wind speed, and conditions.
- Switch between Celsius and Fahrenheit for temperature and metric/imperial units for wind speed.
- Track a history of recent weather searches with timestamps.
- Enjoy dynamic background visuals that reflect the time of day.

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) version 17 or higher.
- JavaFX SDK.
- Maven (for dependency management).

### Steps
1. **Clone or download the project files.**
   - Ensure all files are placed in a project directory.

2. **Set up JavaFX SDK.**
   - Download JavaFX from the official [Gluon website](https://gluonhq.com/products/javafx/).
   - Extract the SDK and note the path to the `lib` folder.

3. **Compile and run the application:**
   - Open a terminal in the project directory.
   - Use the following Maven commands:
     ```bash
     mvn clean install
     mvn javafx:run
     ```

### For Manual Compilation (without Maven):
   - Compile:
     ```bash
     javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d bin src/*.java
     ```
   - Run:
     ```bash
     java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp bin WeatherApp
     ```

## API Details
The application uses the OpenWeatherMap API to fetch real-time weather data.

### Key Points:
- **Base URL:** `https://api.openweathermap.org/data/2.5/weather`
- **Authentication:** Requires a free API key from [OpenWeatherMap](https://openweathermap.org/api).
- **Parameters Used:**
  - `q`: City name (e.g., `London`).
  - `units`: Unit system (`metric` for Celsius, `imperial` for Fahrenheit).
  - `appid`: Your API key.
- **Response Data:** Includes fields for `main` (temperature, humidity), `wind` (speed), and `weather` (conditions and icon).

### API Setup:
1. Create a free account on OpenWeatherMap.
2. Generate an API key and replace the placeholder `your_api_key_here` in the source code with your actual key.

---
For issues or inquiries, please contact the developer or refer to the OpenWeatherMap API documentation.

