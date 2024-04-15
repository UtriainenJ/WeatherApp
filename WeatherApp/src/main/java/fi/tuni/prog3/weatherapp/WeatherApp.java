package fi.tuni.prog3.weatherapp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    
    // Control variables
    private int selectedDay = 0;
    
    // Containers
    private VBox[] arrayDays;
    
    // UI elements
    private Scene scene;
    private VBox layout;
    private BorderPane topBar;
    private HBox midBar;
    private VBox weatherPanel;
    private VBox forecastPanel;
    private VBox historyPanel;
    private VBox mapsPanel;
    
    private void buildTopBar() {
        // BorderPane for left + center + right alignment
        this.topBar = new BorderPane();
        
        // Units button on the left
        var unitsButton = new Button("Imperial");
        unitsButton.setPrefWidth(this.topBarButtonWidth);
        this.topBar.setLeft(unitsButton);
        this.topBar.setAlignment(unitsButton, Pos.CENTER);
        
        // Search button on the right
        var searchButton = new Button("Search & Favorites"); // AmE > BrE
        searchButton.setPrefWidth(this.topBarButtonWidth);
        this.topBar.setRight(searchButton);
        this.topBar.setAlignment(searchButton, Pos.CENTER);
        
        // CIty label in the center
        var cityLabel = new Label("Raisio");
        cityLabel.setFont(new Font("C059 Bold", 24)); // Not quite Cooper Black
        this.topBar.setCenter(cityLabel);
    }
    
    private void buildWeatherPanel() {
        // Current weather label
        var weatherLabel = new Label("Current Weather");
        weatherLabel.setStyle("-fx-font-weight: bold");
        weatherLabel.setAlignment(Pos.CENTER);
        weatherLabel.setPrefHeight(this.weatherLabelHeight);
        
        // Weather and temperature bar
        var weatherIconLabel = new Label("weatherIcon");
        var tempLabel = new Label("-5");
        tempLabel.setFont(new Font("System Regular", 70));
        var tempUnitLabel = new Label("°C");
        tempUnitLabel.setAlignment(Pos.TOP_LEFT);
        tempUnitLabel.setFont(new Font("System Regular", 35));
        var weatherAndTempBar = new HBox(weatherIconLabel,
                tempLabel, tempUnitLabel);
        weatherAndTempBar.setAlignment(Pos.CENTER);
        tempUnitLabel.setPrefHeight(this.weatherTempBarHeight);
        
        // "Feels Like" bar
        var feelsLikeLabel = new Label("Feels like:");
        var feelsLikeStatusLabel = new Label("-10");
        feelsLikeStatusLabel.setStyle("-fx-font-weight: bold");
        var feelsLikeUnitLabel = new Label("°C");
        var feelsLikeBar = new HBox(feelsLikeLabel,
                feelsLikeStatusLabel, feelsLikeUnitLabel);
        feelsLikeBar.setAlignment(Pos.CENTER);
        feelsLikeBar.setPrefHeight(this.feelsLikeBarHeight);
        
        // Air quality, rain, wind
        var airQualityLabel = new Label("Air Quality:");
        var airQualityStatusLabel = new Label("Good");
        airQualityStatusLabel.setStyle("-fx-font-weight: bold");
        var rainIconLabel = new Label("rainIcon");
        var rainStatusLabel = new Label("0.0");
        rainStatusLabel.setStyle("-fx-font-weight: bold");
        var rainUnitLabel = new Label("mm");
        var windIconLabel = new Label("windIcon");
        var windStatusLabel = new Label("3");
        windStatusLabel.setStyle("-fx-font-weight: bold");
        var windUnitLabel = new Label("m/s");
        var airQualityBar = new HBox(airQualityLabel, airQualityStatusLabel,
                rainIconLabel, rainStatusLabel, rainUnitLabel,
                windIconLabel, windStatusLabel, windUnitLabel);
        airQualityBar.setAlignment(Pos.CENTER);
        airQualityBar.setPrefHeight(this.airQualityBarHeight);
        
        this.weatherPanel = new VBox(weatherLabel, weatherAndTempBar,
                feelsLikeBar, airQualityBar);
        this.weatherPanel.setStyle("-fx-background-color: #fae49f;");
        this.weatherPanel.setAlignment(Pos.CENTER);
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
    
    private void selectDay(int day) {
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

    @Override
    public void start(Stage stage) {
        buildUI(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}