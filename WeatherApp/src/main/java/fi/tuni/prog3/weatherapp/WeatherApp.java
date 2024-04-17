package fi.tuni.prog3.weatherapp;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * JavaFX Weather Application.
 */
public class WeatherApp extends Application {
    // Constants
    private final int forecastDays = 5;
    private final int forecastHours = 16;
    private final int topBarButtonWidth = 150;
    private final int weatherLabelHeight = 30;
    private final int weatherTempBarHeight = 80;
    private final int feelsLikeBarHeight = 20;
    private final int airQualityBarHeight = 40;
    private final String tempFileName = "temp.json";
    private final String defaultLocation = "Raisio";
    
    // Control variables
    private int selectedDay = 0;
    private String currentLocation = this.defaultLocation;
    private String currentUnitSystem;
    
    // Containers
    private VBox[] arrayDays;
    
    // Class entities
    private StorageSystem ss;
    private WeatherAPI api;
    
    // UI containers
    private Scene scene;
    private VBox layout;
    private BorderPane topBar;
    private HBox midBar;
    private VBox weatherPanel;
    private VBox forecastPanel;
    private VBox historyPanel;
    private VBox mapsPanel;
    
    // UI elements
    private Button unitsButton;
    private Label cityLabel;
    private Label currentWeatherIcon;
    private Label currentTempField;
    private Label currentTempUnitField;
    private Label currentFeelsLikeField;
    private Label currentFeelsLikeUnitField;
    private Label currentAirQualityField;
    private Label currentRainIcon;
    private Label currentRainField;
    private Label currentRainUnitField;
    private Label currentWindIcon;
    private Label currentWindField;
    private Label currentWindUnitField;
    
    private void buildTopBar() {
        // BorderPane for left + center + right alignment
        this.topBar = new BorderPane();
        
        // Units button on the left
        this.unitsButton = new Button();
        this.unitsButton.setPrefWidth(this.topBarButtonWidth);
        this.topBar.setLeft(this.unitsButton);
        this.topBar.setAlignment(this.unitsButton, Pos.CENTER);
        
        // Search button on the right
        var searchButton = new Button("Search & Favorites"); // AmE > BrE
        searchButton.setPrefWidth(this.topBarButtonWidth);
        this.topBar.setRight(searchButton);
        this.topBar.setAlignment(searchButton, Pos.CENTER);
        
        // CIty label in the center
        this.cityLabel = new Label("Raisio");
        this.cityLabel.setFont(new Font("C059 Bold", 24)); // Not quite Cooper Black
        this.topBar.setCenter(this.cityLabel);
    }
    
    private void buildWeatherPanel() {
        // Current weather label
        var weatherLabel = new Label("Current Weather");
        weatherLabel.setStyle("-fx-font-weight: bold");
        weatherLabel.setAlignment(Pos.CENTER);
        weatherLabel.setPrefHeight(this.weatherLabelHeight);
        
        // Weather and temperature bar
        this.currentWeatherIcon = new Label("weatherIcon");
        this.currentTempField = new Label();
        this.currentTempField.setFont(new Font("System Regular", 70));
        this.currentTempUnitField = new Label();
        this.currentTempUnitField.setAlignment(Pos.TOP_LEFT);
        this.currentTempUnitField.setFont(new Font("System Regular", 35));
        var weatherAndTempBar = new HBox(this.currentWeatherIcon,
                this.currentTempField, this.currentTempUnitField);
        weatherAndTempBar.setAlignment(Pos.CENTER);
        this.currentTempUnitField.setPrefHeight(this.weatherTempBarHeight);
        
        // "Feels Like" bar
        var feelsLikeLabel = new Label("Feels like:");
        this.currentFeelsLikeField = new Label();
        this.currentFeelsLikeField.setStyle("-fx-font-weight: bold");
        this.currentFeelsLikeUnitField = new Label();
        var feelsLikeBar = new HBox(feelsLikeLabel,
                this.currentFeelsLikeField, this.currentFeelsLikeUnitField);
        feelsLikeBar.setAlignment(Pos.CENTER);
        feelsLikeBar.setPrefHeight(this.feelsLikeBarHeight);
        
        // Air quality, rain, wind
        var airQualityLabel = new Label("Air Quality:");
        this.currentAirQualityField = new Label();
        this.currentAirQualityField.setStyle("-fx-font-weight: bold");
        this.currentRainIcon = new Label("rainIcon");
        this.currentRainField = new Label();
        this.currentRainField.setStyle("-fx-font-weight: bold");
        this.currentRainUnitField = new Label();
        this.currentWindIcon = new Label("windIcon");
        this.currentWindField = new Label();
        this.currentWindField.setStyle("-fx-font-weight: bold");
        this.currentWindUnitField = new Label();
        var airQualityBar = new HBox(airQualityLabel,
                this.currentAirQualityField, this.currentRainIcon,
                this.currentRainField, this.currentRainUnitField,
                this.currentWindIcon, this.currentWindField,
                this.currentWindUnitField);
        airQualityBar.setAlignment(Pos.CENTER);
        airQualityBar.setPrefHeight(this.airQualityBarHeight);
        
        this.weatherPanel = new VBox(weatherLabel, weatherAndTempBar,
                feelsLikeBar, airQualityBar);
        this.weatherPanel.setStyle("-fx-background-color: #fae49f;");
        this.weatherPanel.setAlignment(Pos.CENTER);
    }
    
    private void selectDay(int day) 
            throws ArrayIndexOutOfBoundsException {
        // Check for valid array index
        if ((day < 0) | (day >= this.forecastDays)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        // Reset previous day background
        this.arrayDays[this.selectedDay].setStyle(
                "-fx-background-color: #ffffff;");
        
        // Color new day background
        this.arrayDays[day].setStyle("-fx-background-color: #a5c2f9;");
        this.arrayDays[day].requestFocus();
        
        // Update index variable
        this.selectedDay = day;
    }
    
    private VBox buildForecastDay(int day) {
        // Top: weekday and date bar
        var weekdayLabel = new Label("Tue");
        var dateLabel = new Label("24.10");
        dateLabel.setStyle("-fx-font-weight: bold");
        var weekdayDateBar = new HBox(weekdayLabel, dateLabel);
        
        // Middle: weather icon
        var weatherIconLabel = new Label("weatherIcon");
        
        // Bottom: temperature bar
        var minTempLabel = new Label("-4");
        var sepLabel = new Label("...");
        var maxTempLabel = new Label("-2");
        var tempUnitLabel = new Label("°C");
        var tempBar = new HBox(minTempLabel, sepLabel,maxTempLabel,
                tempUnitLabel);
        
        var forecastDay = new VBox(weekdayDateBar, weatherIconLabel, tempBar);
        
        return forecastDay;
    }
    
    private VBox buildForecastHour(int hourIndex) {
        var hourLabel = new Label(String.valueOf(hourIndex % 24));
        hourLabel.setStyle("-fx-font-weight: bold");
        var weatherIconLabel = new Label("weat");
        var tempStatusLabel = new Label("-3°");
        var windIconLabel = new Label("wind");
        var windSpeedLabel = new Label("3");
        var rainStatusLabel = new Label("0.0");
        var airQualityLabel = new Label("39");
        var forecastHour = new VBox(hourLabel, weatherIconLabel,
                tempStatusLabel, windIconLabel, windSpeedLabel,
                rainStatusLabel,airQualityLabel);
        return forecastHour;
    }
    
    private void buildForecastPanel() {
        // Init day array
        this.arrayDays = new VBox[this.forecastDays];
        
        // Horizontal days bar
        var forecastDaysBar = new HBox();
        forecastDaysBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.forecastDays; i++) {
            var forecastDay = buildForecastDay(i);
            this.arrayDays[i] = forecastDay;
            forecastDaysBar.getChildren().add(forecastDay);
        }
        
        // Horizontal hourly forecast bar
        var forecastHoursBar = new HBox();
        forecastHoursBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.forecastHours; i++) {
            var forecastHour = buildForecastHour(10 + i);
            forecastHoursBar.getChildren().add(forecastHour);
        }
        
        this.forecastPanel = new VBox(forecastDaysBar, forecastHoursBar);
    }
    
    private void buildHistoryPanel() {
        
    }
    
    private void buildMapsPanel() {
        
    }
    
    private void buildUI(Stage stage) {
        // Vertical main layout
        this.layout = new VBox();
        this.scene = new Scene(this.layout);
        stage.setScene(this.scene);
        
        // Horizontal bar with two buttons and label
        buildTopBar();
        this.layout.getChildren().add(this.topBar);
        
        // Current Weather panel
        buildWeatherPanel();
        this.layout.getChildren().add(this.weatherPanel);
        
        // Horizontal bar with three buttons
        var forecastButton = new Button("Forecast");
        var historyButton = new Button("History");
        var mapsButton = new Button("Maps");
        this.midBar = new HBox(forecastButton, historyButton, mapsButton);
        this.layout.getChildren().add(this.midBar);
        
        // Bottom part of window, initially Forecast panel
        buildForecastPanel();
        buildHistoryPanel();
        buildMapsPanel();
        this.layout.getChildren().add(this.forecastPanel);
        
        // Focus middle day by default
        selectDay(this.forecastDays / 2);
    }
    
    private void updateTopBar() {
        //this.currentUnitSystem = this.api.getUnitSystem();
        this.currentUnitSystem = "Metric";
        this.unitsButton.setText(this.currentUnitSystem);
        this.cityLabel.setText(this.currentLocation);
    }
    
    private void updateWeatherPanel() {
        // Weather and temperature bar
        //this.currentWeatherIcon.setGraphic();
        this.currentTempField.setText(String.valueOf(
                this.api.getWeather().getMain().getTemp()));
        //this.currentTempUnitField.setText(this.api.getTempUnit());
        this.currentTempUnitField.setText("K");
        
        // "Feels Like" bar
        this.currentFeelsLikeField.setText(String.valueOf(
                this.api.getWeather().getMain().getFeels_like()));
        this.currentFeelsLikeUnitField.setText("K");
        
        // Air quality, rain and wind bar
        this.currentAirQualityField.setText(this.api.getWeather().getWeather()
                .get(0).getDescription());
        //this.currentRainIcon.setGraphic();
        this.currentRainField.setText(String.valueOf(
                this.api.getWeather().getRain().get1h()));
        this.currentRainUnitField.setText("mm");
        //this.currentWindIcon.setGraphic();
        this.currentWindField.setText(String.valueOf(
                this.api.getWeather().getWind().getSpeed()));
        this.currentWindUnitField.setText("m/s");
    }
    
    private void update(String location) {
        // Request weather and forecast data from api
        this.api.getData(location);
        
        // Update UI fields
        updateTopBar();
        updateWeatherPanel();
    }

    @Override
    public void start(Stage stage) 
            throws IOException {
        
        // Initialize logic
        this.ss = new StorageSystem(this.tempFileName);
        this.api = this.ss.readFromFile();
        
        // Initialize UI
        buildUI(stage);
        
        // Update UI
        update(this.currentLocation);
        
        // Display UI
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}