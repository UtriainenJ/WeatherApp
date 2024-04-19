package fi.tuni.prog3.weatherapp;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private final int feelsLikeBarHeight = 30;
    private final int airQualityBarHeight = 30;
    private final int maxFavorites = 10;
    private final int favoriteNameWidth = 200;
    private final String tempFileName = "temp.json";
    
    // Control variables
    private int selectedDay = 0;
    
    // Containers
    private VBox[] arrayDays;
    private Label[] arrayDayWeekday;
    private Label[] arrayDayDate;
    private ImageView[] arrayDayIcon;
    private Label[] arrayDayTempMin;
    private Label[] arrayDayTempMax;
    private Label[] arrayDayTempUnit;
    private HBox[] arrayFavorites;
    private Label[] arrayFavoriteName;
    private Button[] arrayFavoriteSelect;
    private Button[] arrayFavoriteDelete;
    
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
    private HBox forecastDaysBar;
    private VBox forecast;
    private VBox history;
    private VBox maps;
    
    // UI elements
    private Label cityLabel;
    private ImageView currentWeatherIcon;
    private ImageView currentRainIcon;
    private ImageView currentWindIcon;
    private Label currentTempField;
    private Label currentTempUnitField;
    private Label currentFeelsLikeField;
    private Label currentFeelsLikeUnitField;
    private Label currentAirQualityField;
    private Label currentRainField;
    private Label currentRainUnitField;
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
        currentWeatherIcon = new ImageView();
        currentWeatherIcon.setPreserveRatio(true);
        var currentWeatherLabel = new Label();
        currentWeatherLabel.setGraphic(currentWeatherIcon);
        currentTempField = new Label();
        currentTempField.setFont(new Font("System Regular", 70));
        currentTempUnitField = new Label();
        currentTempUnitField.setAlignment(Pos.TOP_LEFT);
        currentTempUnitField.setFont(new Font("System Regular", 35));
        var weatherAndTempBar = new HBox(currentWeatherLabel,
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
        currentAirQualityField.setPadding(new Insets(0, 30, 0, 0));
        currentRainIcon = new ImageView();
        currentRainIcon.setPreserveRatio(true);
        currentRainIcon.setFitWidth(airQualityBarHeight);
        var currentRainLabel = new Label();
        currentRainLabel.setGraphic(currentRainIcon);
        currentRainField = new Label();
        currentRainField.setStyle("-fx-font-weight: bold");
        currentRainUnitField = new Label();
        currentRainUnitField.setPadding(new Insets(0, 30, 0, 0));
        currentWindIcon = new ImageView();
        currentWindIcon.setPreserveRatio(true);
        currentWindIcon.setFitWidth(airQualityBarHeight);
        var currentWindLabel = new Label();
        currentWindLabel.setGraphic(currentWindIcon);
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
        arrayDays[selectedDay].setStyle("");
        
        // Color new day background
        arrayDays[day].setStyle("-fx-background-color: #a5c2f9;");
        arrayDays[day].requestFocus();
        
        // Update index variable
        selectedDay = day;
    }
    
    private void buildForecastDay(int index) {
        // Top: weekday and date bar
        var weekdayLabel = new Label();
        var dateLabel = new Label();
        dateLabel.setStyle("-fx-font-weight: bold");
        var weekdayDateBar = new HBox(weekdayLabel, dateLabel);
        
        // Middle: weather icon
        var weatherIconImage = new ImageView();
        weatherIconImage.setPreserveRatio(true);
        var weatherIconLabel = new Label();
        weatherIconLabel.setGraphic(weatherIconImage);
        
        // Bottom: temperature bar
        var minTempLabel = new Label();
        var sepLabel = new Label("...");
        sepLabel.setPadding(new Insets(0, 5, 0, 5));
        var maxTempLabel = new Label();
        var tempUnitLabel = new Label();
        var tempBar = new HBox(minTempLabel, sepLabel, maxTempLabel,
                tempUnitLabel);
        tempBar.setAlignment(Pos.CENTER);
        
        var forecastDay = new VBox(weekdayDateBar, weatherIconLabel, tempBar);
        forecastDay.setAlignment(Pos.CENTER);
        forecastDaysBar.getChildren().add(forecastDay);
        
        // Add elements to class arrays
        arrayDays[index] = forecastDay;
        arrayDayWeekday[index] = weekdayLabel;
        arrayDayDate[index] = dateLabel;
        arrayDayIcon[index] = weatherIconImage;
        arrayDayTempMin[index] = minTempLabel;
        arrayDayTempMax[index] = maxTempLabel;
        arrayDayTempUnit[index] = tempUnitLabel;
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
        // Initialize arrays
        arrayDays = new VBox[forecastDays];
        arrayDayWeekday = new Label[forecastDays];
        arrayDayDate = new Label[forecastDays];
        arrayDayIcon = new ImageView[forecastDays];
        arrayDayTempMin = new Label[forecastDays];
        arrayDayTempMax = new Label[forecastDays];
        arrayDayTempUnit = new Label[forecastDays];
        
        // Horizontal days bar
        forecastDaysBar = new HBox();
        forecastDaysBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastDays; i++) {
            buildForecastDay(i);
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
        nameField.setPrefWidth(favoriteNameWidth);
        nameField.setPadding(new Insets(0, 0, 0, 10));
        var selectButton = new Button("Select");
        var deleteButton = new Button("Delete");
        var favorite = new HBox(nameField, selectButton, deleteButton);
        favorite.setAlignment(Pos.CENTER_LEFT);
        favorite.setVisible(false);
        searchLayout.getChildren().add(favorite);
        arrayFavoriteName[index] = nameField;
        arrayFavoriteSelect[index] = selectButton;
        arrayFavoriteDelete[index] = deleteButton;
        arrayFavorites[index] = favorite;
    }
    
    private void buildSearchWindow() {
        // Vertical main layout
        searchLayout = new VBox();
        searchWindow = new Stage();
        searchWindow.setScene(new Scene(searchLayout));
        searchLayout.setAlignment(Pos.CENTER);
        
        // Label and close button at the top
        var topBar = new BorderPane();
        var searchLabel = new Label("Search & Favorites");
        searchLabel.setFont(new Font("C059 Bold", 24));
        var closeButton = new Button("Close"); // Button for closing window
        closeButton.setOnAction((event) -> {searchWindow.close();});
        topBar.setCenter(searchLabel);
        topBar.setRight(closeButton);
        topBar.setAlignment(searchLabel, Pos.CENTER);
        topBar.setAlignment(closeButton, Pos.CENTER);
        searchLayout.getChildren().add(topBar);
        
        // Horizontal row
        var searchField = new TextField(); // Enter text
        searchField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) { // Start search
                api.setLocationActive(searchField.getText());
                update();
            }
        });
        searchField.setPrefWidth(favoriteNameWidth);
        var searchButton = new Button("Search"); // Start search
        searchButton.setOnAction((event) -> {
            api.setLocationActive(searchField.getText());
            update();
        });
        var favoriteButton = new Button("Favorite"); // Add favorite
        favoriteButton.setOnAction((event) -> {
            api.addToFavorites(searchField.getText());
            update();
        });
        var searchRow = new HBox(searchField, searchButton, favoriteButton);
        searchLayout.getChildren().add(searchRow);
        
        // Favorites
        arrayFavorites = new HBox[maxFavorites];
        arrayFavoriteName = new Label[maxFavorites];
        arrayFavoriteSelect = new Button[maxFavorites];
        arrayFavoriteDelete = new Button[maxFavorites];
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
    
    private Image getIcon(String icon) {
        String filename = String.format("icons/%s.png", icon);
        var iconFile = new File(filename);
        var iconImage = new Image(iconFile.toURI().toString());
        return iconImage;
    }
    
    private void updateTopBar() {
        cityLabel.setText(api.getLocationActive());
    }
    
    private void updateWeatherPanel() {
        // Weather and temperature bar
        var iconWeat = getIcon(api.getWeather().getWeather().get(0).getIcon());
        currentWeatherIcon.setImage(iconWeat);
        currentTempField.setText(api.getWeather().getMain().getTemp());
        currentTempUnitField.setText(api.getUnitTemp());
        
        // "Feels Like" bar
        currentFeelsLikeField.setText(api.getWeather().getMain().getFeels_like());
        currentFeelsLikeUnitField.setText(api.getUnitTemp());
        
        // Air quality, rain and wind bar
        currentAirQualityField.setText("TODO");
        currentRainIcon.setImage(getIcon("rain"));
        currentRainField.setText(api.getWeather().getRain().get1h());
        currentRainUnitField.setText(api.getUnitRain());
        currentWindIcon.setImage(getIcon("wind"));
        currentWindField.setText(api.getWeather().getWind().getSpeed());
        currentWindUnitField.setText(api.getUnitWind());
    }
    
    private void updateForecastDay(int index) {
        var forecastDay = api.getForecast().getList().get(index);
        //arrayDayWeekday[index].setText(); TODO: weekday
        arrayDayDate[index].setText(forecastDay.getDt_txt()); // TODO: date
        Image icon = getIcon(forecastDay.getWeather().get(0).getIcon());
        arrayDayIcon[index].setImage(icon);
        arrayDayTempMin[index].setText(forecastDay.getMain().getTemp_min()); // TODO: daily minimum temperature
        arrayDayTempMax[index].setText(forecastDay.getMain().getTemp_max()); // TODO: daily maximum temperature
        arrayDayTempUnit[index].setText(api.getUnitTemp());
    }
    
    private void updateForecast() {
        for (int i = 0; i < forecastDays; i++) {
            updateForecastDay(i);
        }
    }
    
    private void updateFavoritesIndex(int index, String loc) {
        //selectButton.setOnAction((event) -> {api.setLocationActive(loc);});
        //deleteButton.setOnAction((event) -> {api.removeFromFavorites(loc);});
        arrayFavoriteName[index].setText(loc);
        arrayFavoriteSelect[index].setOnAction((event) -> {
            api.setLocationActive(loc);
            update();
        });
        arrayFavoriteDelete[index].setOnAction((event) -> {
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
        
        // Update bottom panel
        updateForecast();
        
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