package fi.tuni.prog3.weatherapp;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.round;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private final double dailyForecastWidth = 96;
    private final double hourlyForecastWidth = 30;
    private final String tempFileName = "temp.json";
    
    // Control variables
    private int selectedDay = 0;
    private double sliderValue = 0;
    
    // Containers
    private VBox[] arrayDays;
    private VBox[] arrayHours;
    private ImageView[] arrayDayIcon;
    private ImageView[] arrayHourWeatherIcon;
    private ImageView[] arrayHourWindIcon;
    private Label[] arrayDayWeekday;
    private Label[] arrayDayDate;
    private Label[] arrayDayTempMin;
    private Label[] arrayDayTempMax;
    private Label[] arrayDayTempUnit;
    private Label[] arrayHourLabel;
    private Label[] arrayHourTemp;
    private Label[] arrayHourWind;
    private Label[] arrayHourRainStat;
    private Label[] arrayHourRainPerc;
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
    private HBox forecastHoursBar;
    private VBox forecast;
    private VBox history;
    private VBox maps;
    
    // UI elements
    private ImageView currentWeatherIcon;
    private ImageView currentRainIcon;
    private ImageView currentWindIcon;
    private Label cityLabel;
    private Label currentTempField;
    private Label currentTempUnitField;
    private Label currentFeelsLikeField;
    private Label currentFeelsLikeUnitField;
    private Label currentAirQualityField;
    private Label currentRainField;
    private Label currentRainUnitField;
    private Label currentWindField;
    private Label currentWindUnitField;
    private Label searchStatusField;
    private TextField searchField;
    private Slider forecastSlider;
    
    private void buildTopBar() {
        // BorderPane for left + center + right alignment
        topBar = new BorderPane();
        
        // Units button on the left
        var unitsButton = new Button("Switch Units");
        unitsButton.setPrefWidth(topBarButtonWidth);
        unitsButton.setOnAction((event) -> {
            api.switchUnits();
            update();
        });
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
        currentTempField.setPadding(new Insets(0, 0, 0, 10));
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
        currentAirQualityField.setPadding(new Insets(0, 30, 0, 5));
        currentRainIcon = new ImageView();
        currentRainIcon.setPreserveRatio(true);
        currentRainIcon.setFitWidth(airQualityBarHeight);
        var currentRainLabel = new Label();
        currentRainLabel.setGraphic(currentRainIcon);
        currentRainField = new Label();
        currentRainField.setStyle("-fx-font-weight: bold");
        currentRainField.setPadding(new Insets(0, 5, 0, 5));
        currentRainUnitField = new Label();
        currentRainUnitField.setPadding(new Insets(0, 30, 0, 0));
        currentWindIcon = new ImageView();
        currentWindIcon.setPreserveRatio(true);
        currentWindIcon.setFitWidth(airQualityBarHeight);
        var currentWindLabel = new Label();
        currentWindLabel.setGraphic(currentWindIcon);
        currentWindField = new Label();
        currentWindField.setStyle("-fx-font-weight: bold");
        currentWindField.setPadding(new Insets(0, 5, 0, 5));
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
        weekdayLabel.setPadding(new Insets(0, 5, 0, 0));
        var dateLabel = new Label();
        dateLabel.setStyle("-fx-font-weight: bold");
        var weekdayDateBar = new HBox(weekdayLabel, dateLabel);
        weekdayDateBar.setAlignment(Pos.CENTER);
        
        // Middle: weather icon
        var weatherIconImage = new ImageView();
        weatherIconImage.setPreserveRatio(true);
        weatherIconImage.setFitWidth(dailyForecastWidth);
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
        forecastDay.setOnMouseClicked((event) -> {
            selectDay(index);
            forecastSlider.setValue(0); // Reset hourly slider
            sliderValue = 0; // Reset slider value too
            updateForecast(); // Update
        });
        forecastDay.setAlignment(Pos.CENTER);
        forecastDay.setPrefWidth(dailyForecastWidth);
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
    
    private void buildForecastHour(int index) {
        // Hour
        var hourLabel = new Label();
        hourLabel.setStyle("-fx-font-weight: bold");
        
        //Weather icon
        var weatherIconImage = new ImageView();
        weatherIconImage.setPreserveRatio(true);
        weatherIconImage.setFitWidth(hourlyForecastWidth);
        var weatherIconLabel = new Label();
        weatherIconLabel.setGraphic(weatherIconImage);
        
        // Temperature
        var tempStatusLabel = new Label();
        
        // Wind
        var windIconImage = new ImageView(getIcon("compass"));
        windIconImage.setPreserveRatio(true);
        windIconImage.setFitWidth(hourlyForecastWidth);
        var windIconLabel = new Label();
        windIconLabel.setGraphic(windIconImage);
        var windStatusLabel = new Label();
        
        // Rain
        var rainStatusLabel = new Label();
        var rainPercLabel = new Label();
        
        // UI element
        var forecastHour = new VBox(hourLabel, weatherIconLabel,
                tempStatusLabel, windIconLabel, windStatusLabel,
                rainStatusLabel, rainPercLabel);
        forecastHour.setAlignment(Pos.CENTER);
        forecastHour.setPrefWidth(hourlyForecastWidth);
        forecastHoursBar.getChildren().add(forecastHour);
        
        // Add elements to class arrays
        arrayHours[index] = forecastHour;
        arrayHourWeatherIcon[index] = weatherIconImage;
        arrayHourWindIcon[index] = windIconImage;
        arrayHourLabel[index] = hourLabel;
        arrayHourTemp[index] = tempStatusLabel;
        arrayHourWind[index] = windStatusLabel;
        arrayHourRainStat[index] = rainStatusLabel;
        arrayHourRainPerc[index] = rainPercLabel;
    }
    
    private void buildForecast() {
        // Initialize arrays
        arrayDays = new VBox[forecastDays];
        arrayDayIcon = new ImageView[forecastDays];
        arrayDayWeekday = new Label[forecastDays];
        arrayDayDate = new Label[forecastDays];
        arrayDayTempMin = new Label[forecastDays];
        arrayDayTempMax = new Label[forecastDays];
        arrayDayTempUnit = new Label[forecastDays];
        
        arrayHours = new VBox[forecastHours];
        arrayHourWeatherIcon = new ImageView[forecastHours];
        arrayHourWindIcon = new ImageView[forecastHours];
        arrayHourLabel = new Label[forecastHours];
        arrayHourTemp = new Label[forecastHours];
        arrayHourWind = new Label[forecastHours];
        arrayHourRainStat = new Label[forecastHours];
        arrayHourRainPerc = new Label[forecastHours];
        
        // Horizontal days bar
        forecastDaysBar = new HBox();
        forecastDaysBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastDays; i++) {
            buildForecastDay(i);
        }
        
        // Horizontal hourly forecast bar
        forecastHoursBar = new HBox();
        forecastHoursBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastHours; i++) {
            buildForecastHour(i);
        }
        
        // Hourly forecast slider
        forecastSlider = new Slider();
        forecastSlider.setMin(0);
        forecastSlider.setMax(24 - forecastHours);
        forecastSlider.setMinorTickCount(0);
        forecastSlider.setMajorTickUnit(1);
        forecastSlider.setSnapToTicks(true);
        forecastSlider.setShowTickMarks(true);
        forecastSlider.setOnMouseDragged((event) -> {
            double prevSliderValue = sliderValue;
            sliderValue = forecastSlider.getValue() / forecastSlider.getMax();
            if (sliderValue != prevSliderValue) {
                updateForecast();
            }
        });
        
        forecast = new VBox(forecastDaysBar, forecastHoursBar, forecastSlider);
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
    
    private void search(String location) {
        String previousLocation = api.getLocationActive();
        api.setLocationActive(location);
        boolean locationFound = update();
        if (locationFound) {
            searchStatusField.setText("Search Successful");
        } else {
            searchStatusField.setText("Try Again");
            api.setLocationActive(previousLocation);
        }
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
        
        // Search status field
        searchStatusField = new Label();
        searchStatusField.setPadding(new Insets(5, 0, 5, 10));
        searchLayout.getChildren().add(searchStatusField);
        searchLayout.setAlignment(Pos.CENTER_LEFT);
        
        // Horizontal row
        searchField = new TextField(); // Enter text
        searchField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) { // Start search
                search(searchField.getText());
            } else {
                searchStatusField.setText("");
            }
        });
        searchField.setPrefWidth(favoriteNameWidth);
        var searchButton = new Button("Search"); // Start search
        searchButton.setOnAction((event) -> {search(searchField.getText());});
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
        
        // Focus today by default
        selectDay(0);
        
        // "Search & Favorites" window
        buildSearchWindow();
    }
    
    private Image getIcon(String icon) {
        String filename = String.format("icons/%s.png", icon);
        try { // Find icon
            final InputStream iconFile = new DataInputStream(
                    new FileInputStream(filename));
            var iconImage = new Image(iconFile);
            return iconImage;
        } catch (IOException ex1) { // Icon missing. get default
            try {
                final InputStream defaultFile = new DataInputStream(
                        new FileInputStream("icons/default.png"));
                var iconImage = new Image(defaultFile);
                return iconImage;
            } catch (IOException ex2) { // Both missing
                return null;
            }
        }
    }
    
    private void updateTopBar() {
        cityLabel.setText(api.getLocationActive());
    }
    
    private void updateWeatherPanel() {
        var weather = api.getWeather();
        
        // Weather and temperature bar
        var iconWeat = getIcon(weather.getWeather().get(0).getIcon());
        currentWeatherIcon.setImage(iconWeat);
        currentTempField.setText(weather.getMain().getTemp());
        currentTempUnitField.setText(api.getUnitTemp());
        
        // "Feels Like" bar
        currentFeelsLikeField.setText(weather.getMain().getFeels_like());
        currentFeelsLikeUnitField.setText(api.getUnitTemp());
        
        // Air quality, rain and wind bar
        currentAirQualityField.setText(weather.getAirQuality());
        currentRainIcon.setImage(getIcon("rain"));
        currentRainField.setText(weather.getRain().get1h());
        currentRainUnitField.setText(api.getUnitRain());
        currentWindIcon.setImage(getIcon("wind"));
        currentWindField.setText(weather.getWind().getSpeed());
        currentWindUnitField.setText(api.getUnitWind());
    }
    
    private void updateForecastDay(int index) {
        var forecastDay = api.getForecastDaily().getList().get(index);
        Image icon = getIcon(forecastDay.getWeather().get(0).getIcon());
        arrayDayIcon[index].setImage(icon);
        arrayDayWeekday[index].setText(forecastDay.getWeekday());
        arrayDayDate[index].setText(forecastDay.getDate());
        arrayDayTempMin[index].setText(forecastDay.getTemp().getMax());
        arrayDayTempMax[index].setText(forecastDay.getTemp().getMin());
        arrayDayTempUnit[index].setText(api.getUnitTemp());
    }
    
    private void updateForecastHour(int bias, int index) {
        var forecast = api.getForecastHourly().getList();
        if ((bias + index < forecast.size()) & (bias + index >= 0)) {
            var forecastHour = forecast.get(bias + index);
            Image icon = getIcon(forecastHour.getWeather().get(0).getIcon());
            arrayHourWeatherIcon[index].setImage(icon);
            double rot = Double.parseDouble(forecastHour.getWind().getDeg());
            arrayHourWindIcon[index].setRotate(rot);
            arrayHourLabel[index].setText(forecastHour.getHour());
            arrayHourTemp[index].setText(forecastHour.getMain().getTemp());
            arrayHourWind[index].setText(forecastHour.getWind().getSpeed());
            arrayHourRainStat[index].setText(forecastHour.getRain().get1h());
            arrayHourRainPerc[index].setText(forecastHour.getPop());
            arrayHours[index].setVisible(true);
        } else { // Forecast doesn't go this far (forward or back)
            arrayHours[index].setVisible(false);
        }
    }
    
    private void updateForecast() {
        for (int i = 0; i < forecastDays; i++) {
            updateForecastDay(i);
        }
        
        int buffer = 24 - forecastHours;
        int currentHour = Integer.parseInt(
                api.getForecastHourly().getList().get(0).getHour());
        int biasDays = 24 * selectedDay;
        int biasHours =  (int)round(sliderValue * buffer);
        if ((selectedDay == 0) & (biasHours < currentHour)) {
            forecastSlider.setDisable(true);
            if (currentHour < buffer) {
                biasHours = currentHour;
            } else {
                biasHours = buffer;
            }
        } else if ((selectedDay >= forecastDays - 1) & (currentHour > buffer)) {
            forecastSlider.setDisable(true);
        } else {
            forecastSlider.setDisable(false);
        }
        int bias = biasDays + biasHours - currentHour;
        for (int i = 0; i < forecastHours; i++) {
            updateForecastHour(bias, i);
        }
    }
    
    private void updateFavoritesIndex(int index, String loc) {
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
    
    private boolean update() {
        // Request weather and forecast data from api
        boolean requestSuccess = api.getData();
        if (requestSuccess) {
            // Update main window UI fields
            updateTopBar();
            updateWeatherPanel();
        
            // Update bottom panel
            updateForecast();
        
            // Update search window UI fields
            searchStatusField.setText("");
            updateFavorites();
        }
        return requestSuccess;
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