package fi.tuni.prog3.weatherapp;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
    private final int maxFavorites = 10;
    private final String tempFileName = "temp.json";
    
    // Control variables
    private int selectedDay = 0;
    
    // Containers
    private VBox[] arrayDays;
    private HBox[] arrayFavorites;
    private Label[] arrayName;
    private Button[] arraySelect;
    private Button[] arrayDelete;
    
    // Class entities
    private StorageSystem ss;
    private WeatherAPI api;
    
    // UI containers
    private Stage mainWindow;
    private Stage searchWindow;
    private VBox mainLayout;
    private VBox searchLayout;
    private BorderPane topBar;
    private HBox midBar;
    private StackPane basePanel;
    private VBox weather;
    private VBox forecast;
    private VBox history;
    private VBox maps;
    
    // UI elements
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
    
    private void switchUnits() {
        switch(api.getUnit().toLowerCase()) {
            case "metric":
                api.setUnits("imperial");
                break;
            case "imperial":
                api.setUnits("metric");
                break;
        }
        update();
    }
    
    private void buildTopBar() {
        // BorderPane for left + center + right alignment
        topBar = new BorderPane();
        
        // Units button on the left
        var unitsButton = new Button("Switch Units");
        unitsButton.setPrefWidth(topBarButtonWidth);
        unitsButton.setOnAction((event) -> {switchUnits();});
        topBar.setLeft(unitsButton);
        topBar.setAlignment(unitsButton, Pos.CENTER);
        
        // Search button on the right
        var searchButton = new Button("Search & Favorites"); // AmE > BrE
        searchButton.setPrefWidth(topBarButtonWidth);
        searchButton.setOnAction((event) -> {searchWindow.show();});
        topBar.setRight(searchButton);
        topBar.setAlignment(searchButton, Pos.CENTER);
        
        // City label in the center
        cityLabel = new Label();
        cityLabel.setFont(new Font("C059 Bold", 24)); // Not quite Cooper Black
        topBar.setCenter(cityLabel);
    }
    
    private void buildWeather() {
        // Current weather label
        var weatherLabel = new Label("Current Weather");
        weatherLabel.setStyle("-fx-font-weight: bold");
        weatherLabel.setAlignment(Pos.CENTER);
        weatherLabel.setPrefHeight(weatherLabelHeight);
        
        // Weather and temperature bar
        currentWeatherIcon = new Label("weatherIcon");
        currentTempField = new Label();
        currentTempField.setFont(new Font("System Regular", 70));
        currentTempUnitField = new Label();
        currentTempUnitField.setAlignment(Pos.TOP_LEFT);
        currentTempUnitField.setFont(new Font("System Regular", 35));
        var weatherAndTempBar = new HBox(currentWeatherIcon,
                currentTempField, currentTempUnitField);
        weatherAndTempBar.setAlignment(Pos.CENTER);
        currentTempUnitField.setPrefHeight(weatherTempBarHeight);
        
        // "Feels Like" bar
        var feelsLikeLabel = new Label("Feels like:");
        currentFeelsLikeField = new Label();
        currentFeelsLikeField.setStyle("-fx-font-weight: bold");
        currentFeelsLikeUnitField = new Label();
        var feelsLikeBar = new HBox(feelsLikeLabel,
                currentFeelsLikeField, currentFeelsLikeUnitField);
        feelsLikeBar.setAlignment(Pos.CENTER);
        feelsLikeBar.setPrefHeight(feelsLikeBarHeight);
        
        // Air quality, rain, wind
        var airQualityLabel = new Label("Air Quality:");
        currentAirQualityField = new Label();
        currentAirQualityField.setStyle("-fx-font-weight: bold");
        currentRainIcon = new Label("rainIcon");
        currentRainField = new Label();
        currentRainField.setStyle("-fx-font-weight: bold");
        currentRainUnitField = new Label();
        currentWindIcon = new Label("windIcon");
        currentWindField = new Label();
        currentWindField.setStyle("-fx-font-weight: bold");
        currentWindUnitField = new Label();
        var airQualityBar = new HBox(airQualityLabel,
                currentAirQualityField, currentRainIcon,
                currentRainField, currentRainUnitField,
                currentWindIcon, currentWindField,
                currentWindUnitField);
        airQualityBar.setAlignment(Pos.CENTER);
        airQualityBar.setPrefHeight(airQualityBarHeight);
        
        weather = new VBox(weatherLabel, weatherAndTempBar,
                feelsLikeBar, airQualityBar);
        weather.setStyle("-fx-background-color: #fae49f;");
        weather.setAlignment(Pos.CENTER);
    }
    
    private void selectDay(int day) 
            throws ArrayIndexOutOfBoundsException {
        // Check for valid array index
        if ((day < 0) | (day >= forecastDays)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        // Reset previous day background
        arrayDays[selectedDay].setStyle(
                "-fx-background-color: #ffffff;");
        
        // Color new day background
        arrayDays[day].setStyle("-fx-background-color: #a5c2f9;");
        arrayDays[day].requestFocus();
        
        // Update index variable
        selectedDay = day;
    }
    
    private VBox buildForecastDay() {
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
    
    private void buildForecast() {
        // Init day array
        arrayDays = new VBox[forecastDays];
        
        // Horizontal days bar
        var forecastDaysBar = new HBox();
        forecastDaysBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastDays; i++) {
            var forecastDay = buildForecastDay();
            arrayDays[i] = forecastDay;
            forecastDaysBar.getChildren().add(forecastDay);
        }
        
        // Horizontal hourly forecast bar
        var forecastHoursBar = new HBox();
        forecastHoursBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastHours; i++) {
            var forecastHour = buildForecastHour(10 + i);
            forecastHoursBar.getChildren().add(forecastHour);
        }
        
        forecast = new VBox(forecastDaysBar, forecastHoursBar);
    }
    
    private void buildHistory() {
        history = new VBox();
        history.getChildren().add(new Label("History"));
    }
    
    private void buildMaps() {
        maps = new VBox();
        maps.getChildren().add(new Label("Maps"));
    }
    
    private void selectPanel(Node panel) 
            throws ArrayIndexOutOfBoundsException {
        
        boolean panelFound = false;
        for (Node node : basePanel.getChildren()) {
            if (node == panel) {
                panelFound = true;
                node.setVisible(true);
            } else {
                node.setVisible(false);
            }
        }
        
        if (panelFound == false) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    private void buildSearchFavorite(int index) {
        var nameField = new Label();
        var selectButton = new Button("Select");
        var deleteButton = new Button("Delete");
        var favorite = new HBox(nameField, selectButton, deleteButton);
        favorite.setAlignment(Pos.CENTER_RIGHT);
        favorite.setVisible(false);
        searchLayout.getChildren().add(favorite);
        arrayName[index] = nameField;
        arraySelect[index] = selectButton;
        arrayDelete[index] = deleteButton;
        arrayFavorites[index] = favorite;
    }
    
    private void buildSearchWindow() {
        // Vertical main layout
        searchLayout = new VBox();
        searchWindow = new Stage();
        searchWindow.setScene(new Scene(searchLayout));
        searchLayout.setAlignment(Pos.CENTER);
        
        // Label at the top
        var searchLabel = new Label("Search & Favorites");
        searchLabel.setFont(new Font("C059 Bold", 24));
        searchLayout.getChildren().add(searchLabel);
        
        // Horizontal row
        var searchField = new TextField(); // Field for entering text
        var searchButton = new Button("Search"); // Button for starting search
        searchButton.setOnAction((event) -> {
            api.setLocationActive(searchField.getText());
            update();
            });
        var favoriteButton = new Button("Favorite");
        favoriteButton.setOnAction((event) -> {
            api.addToFavorites(searchField.getText());
            update();
            });
        var closeButton = new Button("Close"); // Button for closing window
        closeButton.setOnAction((event) -> {searchWindow.close();});
        var searchRow = new HBox(searchField, searchButton, favoriteButton,
                closeButton);
        searchLayout.getChildren().add(searchRow);
        
        // Favorites
        arrayFavorites = new HBox[maxFavorites];
        arrayName = new Label[maxFavorites];
        arraySelect = new Button[maxFavorites];
        arrayDelete = new Button[maxFavorites];
        for (int index = 0; index < maxFavorites; index++) {
            buildSearchFavorite(index);
        }
    }
    
    private void buildUI() {
        // Vertical main layout
        mainLayout = new VBox();
        mainWindow.setScene(new Scene(mainLayout));
        
        // Horizontal bar with two buttons and label
        buildTopBar();
        mainLayout.getChildren().add(topBar);
        
        // Current Weather panel
        buildWeather();
        mainLayout.getChildren().add(weather);
        
        // Horizontal bar with three buttons
        var forecastButton = new Button("Forecast");
        forecastButton.setOnAction((event) -> {selectPanel(forecast);});
        var historyButton = new Button("History");
        historyButton.setOnAction((event) -> {selectPanel(history);});
        var mapsButton = new Button("Maps");
        mapsButton.setOnAction((event) -> {selectPanel(maps);});
        midBar = new HBox(forecastButton, historyButton, mapsButton);
        mainLayout.getChildren().add(midBar);
        
        // Bottom part of window, initially Forecast panel
        buildForecast();
        buildHistory();
        buildMaps();
        basePanel = new StackPane(forecast, history, maps);
        mainLayout.getChildren().add(basePanel);
        selectPanel(forecast);
        
        // Focus middle day by default
        selectDay(forecastDays / 2);
        
        // "Search & Favorites" window
        buildSearchWindow();
    }
    
    private void updateTopBar() {
        cityLabel.setText(api.getLocationActive());
    }
    
    private void updateWeatherPanel() {
        // Weather and temperature bar
        //currentWeatherIcon.setGraphic();
        currentTempField.setText(api.getWeather().getMain().getTemp());
        currentTempUnitField.setText(api.getUnitTemp());
        
        // "Feels Like" bar
        currentFeelsLikeField.setText(api.getWeather().getMain().getFeels_like());
        currentFeelsLikeUnitField.setText(api.getUnitTemp());
        
        // Air quality, rain and wind bar
        //currentAirQualityField.setText(api.getWeather());
        currentAirQualityField.setText("TODO");
        //currentRainIcon.setGraphic();
        currentRainField.setText(api.getWeather().getRain().get1h());
        currentRainUnitField.setText(api.getUnitRain());
        //currentWindIcon.setGraphic();
        currentWindField.setText(api.getWeather().getWind().getSpeed());
        currentWindUnitField.setText(api.getUnitWind());
    }
    
    private void updateFavoritesIndex(int index, String loc) {
        //selectButton.setOnAction((event) -> {api.setLocationActive(loc);});
        //deleteButton.setOnAction((event) -> {api.removeFromFavorites(loc);});
        arrayName[index].setText(loc);
        arraySelect[index].setOnAction((event) -> {
            api.setLocationActive(loc);
            update();
            });
        arrayDelete[index].setOnAction((event) -> {
            api.removeFromFavorites(loc);
            update();
            });
    }
    
    private void updateFavorites() {
        int size = api.getLocationFavorites().size();
        for (int index = 0; index < maxFavorites; index++) {
            if (index < size) {
                updateFavoritesIndex(index,
                        api.getLocationFavorites().get(index));
                arrayFavorites[index].setVisible(true);
            } else {
                arrayFavorites[index].setVisible(false);
            }
        }
    }
    
    private void update() {
        // Request weather and forecast data from api
        api.getData();
        
        // Update main window UI fields
        updateTopBar();
        updateWeatherPanel();
        
        // Update search window UI fields
        updateFavorites();
    }

    @Override
    public void start(Stage stage) 
            throws IOException {
        
        // Initialize logic
        ss = new StorageSystem(tempFileName);
        api = ss.readFromFile();
        
        // Initialize UI
        mainWindow = stage;
        buildUI();
        
        // Update UI
        update();
        
        // Display UI
        stage.show();
    }
    
    @Override
    public void stop() 
            throws IOException {
        ss.writeToFile(api);
    }

    public static void main(String[] args) {
        launch();
    }
}