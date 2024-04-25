package fi.tuni.prog3.weatherapp;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.round;
import java.util.Locale;
import javafx.application.Application;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * JavaFX Weather Application.
 * <p>
 * @author Joonas Pinomäki
 * @author Jerri Tarpio
 * @author Jaakko Utriainen
 * @since 1.0
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
    private final int moveButtonLength = 40;
    private final int moveButtonWidth = moveButtonLength;
    private final int maxFavorites = 8;
    private final int maxHistory = 8;
    private final int searchTextWidth = 200;
    private final double dailyForecastWidth = 96;
    private final double hourlyForecastWidth = 30;
    private final double rainMeterHeight = 20;
    private final double mapHeight = 240;
    private final String tempFileName = "temp.json";
    
    // Cell styles
    private final String styleYellow = "-fx-background-color: #fae49f;";
    private final String styleBlue = "-fx-background-color: #a5c2f9;";
    private final String styleNoData = "-fx-background-color: #949494;";
    private final String[] styleRain = {
        "",
        "-fx-background-color: #078500;",
        "-fx-background-color: #00e1ff;",
        "-fx-background-color: #0096bb;",
        "-fx-background-color: #004488;"
    };
    
    // Control variables
    private int selectedDay = 0;
    private double sliderValue = 0;
    
    // Element arrays
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
    private Label[] arrayHistoryName;
    private Label[] arrayFavoriteName;
    private Button[] arrayHistorySelect;
    private Button[] arrayHistoryFavorite;
    private Button[] arrayFavoriteSelect;
    private Button[] arrayFavoriteDelete;
    private Label[][] arrayRainMeters;
    
    // Class entities
    private StorageSystem ss;
    private WeatherAPI api;
    
    // UI containers
    private Stage mainWindow;
    private Stage searchWindow;
    private VBox mainLayout;
    private GridPane searchLayout;
    private BorderPane topBar;
    private HBox midBar;
    private StackPane basePanel;
    private VBox weather;
    private HBox fcDaysBar;
    private HBox fcHoursBar;
    private VBox forecastPanel;
    private VBox historyPanel;
    private VBox mapsPanel;
    
    // UI elements
    private ImageView currentWeatherIcon;
    private ImageView currentRainIcon;
    private ImageView currentWindIcon;
    private ImageView mapImage;
    private ImageView mapImageBase;
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
    private Slider fcSlider;
    
    /**
     * Builds the top bar of the main window.
     */
    private void buildTopBar() {
        // BorderPane for left + center + right alignment
        topBar = new BorderPane();
        
        // Units button on the left
        var unitsButton = new Button("Switch Units");
        unitsButton.setOnAction((event) -> {
            api.switchUnits();
            update();
        });
        unitsButton.setPrefWidth(topBarButtonWidth);
        unitsButton.setMaxHeight(Double.MAX_VALUE);
        topBar.setLeft(unitsButton);
        topBar.setAlignment(unitsButton, Pos.CENTER);
        
        // Search button on the right
        var searchButton = new Button("Search & Favorites"); // AmE > BrE
        searchButton.setOnAction((event) -> {searchWindow.show();});
        searchButton.setPrefWidth(topBarButtonWidth);
        searchButton.setMaxHeight(Double.MAX_VALUE);
        topBar.setRight(searchButton);
        topBar.setAlignment(searchButton, Pos.CENTER);
        
        // City label in the center
        cityLabel = new Label();
        cityLabel.setFont(new Font("C059 Bold", 24)); // Not quite Cooper Black
        topBar.setCenter(cityLabel);
    }
    
    /**
     * Builds the main weather panel.
     * This is at the top of the main window.
     */
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
        weather.setStyle(styleYellow);
        weather.setAlignment(Pos.CENTER);
    }
    
    /**
     * Switches between the bottom panels of the UI
     * @param panel The root of the requested panel element
     * @throws ArrayIndexOutOfBoundsException
     */
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
    
    /**
     * Controls which day the hourly forecast is displayed for.
     * @param day The index of the day that is requested for selection
     * @throws ArrayIndexOutOfBoundsException
     */
    private void selectDay(int day) 
            throws ArrayIndexOutOfBoundsException {
        // Check for valid array index
        if ((day < 0) | (day >= forecastDays)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        // Reset previous day background
        arrayDays[selectedDay].setStyle("");
        
        // Color new day background
        arrayDays[day].setStyle(styleBlue);
        arrayDays[day].requestFocus();
        
        // Update index variable
        selectedDay = day;
    }
    
    /**
     * Builds one day of the daily forecast in the Forecast panel.
     * @param day The index of the day to be built
     */
    private void buildForecastDay(int day) {
        // Weekday and date
        var weekdayLabel = new Label();
        weekdayLabel.setPadding(new Insets(0, 5, 0, 0));
        var dateLabel = new Label();
        dateLabel.setStyle("-fx-font-weight: bold");
        var dateBar = new HBox(weekdayLabel, dateLabel);
        dateBar.setAlignment(Pos.CENTER);
        
        // Weather icon
        var weatherIconImage = new ImageView();
        weatherIconImage.setPreserveRatio(true);
        weatherIconImage.setFitWidth(dailyForecastWidth);
        var weatherIcon = new Label();
        weatherIcon.setGraphic(weatherIconImage);
        
        // Maximum and minimum temperature
        var minTempLabel = new Label();
        var sepLabel = new Label("...");
        sepLabel.setPadding(new Insets(0, 5, 0, 5));
        var maxTempLabel = new Label();
        var tempUnitLabel = new Label();
        var tempBar = new HBox(minTempLabel, sepLabel, maxTempLabel,
                tempUnitLabel);
        tempBar.setAlignment(Pos.CENTER);
        
        // Rain meter
        var rainMeter = new HBox();
        for (int hour = 0; hour < 24; hour++) {
            var hourLabel = new Label();
            hourLabel.setPrefSize(dailyForecastWidth / 24, rainMeterHeight);
            rainMeter.getChildren().add(hourLabel);
            arrayRainMeters[day][hour] = hourLabel;
        }
        
        // Build UI container
        var forecastDay = new VBox(dateBar, weatherIcon, tempBar, rainMeter);
        forecastDay.setOnMouseClicked((event) -> {
            selectDay(day);
            fcSlider.setValue(0); // Reset hourly slider
            sliderValue = 0; // Reset slider value too
            updateForecast(); // Update
        });
        forecastDay.setAlignment(Pos.CENTER);
        forecastDay.setPrefWidth(dailyForecastWidth);
        fcDaysBar.getChildren().add(forecastDay);
        
        // Add elements to class arrays
        arrayDays[day] = forecastDay;
        arrayDayWeekday[day] = weekdayLabel;
        arrayDayDate[day] = dateLabel;
        arrayDayIcon[day] = weatherIconImage;
        arrayDayTempMin[day] = minTempLabel;
        arrayDayTempMax[day] = maxTempLabel;
        arrayDayTempUnit[day] = tempUnitLabel;
    }
    
    /**
     * Builds one hour of the hourly forecast in the Forecast panel.
     * @param index The index of the elements in the UI arrays
     */
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
        fcHoursBar.getChildren().add(forecastHour);
        
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
    
    /**
     * Builds the Forecast panel.
     * This is the bottom half of the main window by default.
     */
    private void buildForecastPanel() {
        // Initialize arrays
        arrayDays = new VBox[forecastDays];
        arrayDayIcon = new ImageView[forecastDays];
        arrayDayWeekday = new Label[forecastDays];
        arrayDayDate = new Label[forecastDays];
        arrayDayTempMin = new Label[forecastDays];
        arrayDayTempMax = new Label[forecastDays];
        arrayDayTempUnit = new Label[forecastDays];
        arrayRainMeters = new Label[forecastDays][24];
        
        arrayHours = new VBox[forecastHours];
        arrayHourWeatherIcon = new ImageView[forecastHours];
        arrayHourWindIcon = new ImageView[forecastHours];
        arrayHourLabel = new Label[forecastHours];
        arrayHourTemp = new Label[forecastHours];
        arrayHourWind = new Label[forecastHours];
        arrayHourRainStat = new Label[forecastHours];
        arrayHourRainPerc = new Label[forecastHours];
        
        // Horizontal days bar
        fcDaysBar = new HBox();
        fcDaysBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastDays; i++) {
            buildForecastDay(i);
        }
        
        // Horizontal hourly forecast bar
        fcHoursBar = new HBox();
        fcHoursBar.setAlignment(Pos.CENTER);
        for (int i = 0; i < forecastHours; i++) {
            buildForecastHour(i);
        }
        
        // Hourly forecast slider
        fcSlider = new Slider();
        fcSlider.setMin(0);
        fcSlider.setMax(24 - forecastHours);
        fcSlider.setMinorTickCount(0);
        fcSlider.setMajorTickUnit(1);
        fcSlider.setSnapToTicks(true);
        fcSlider.setShowTickMarks(true);
        fcSlider.setOnMouseDragged((event) -> {
            double prevSliderValue = sliderValue;
            sliderValue = fcSlider.getValue() / fcSlider.getMax();
            if (sliderValue != prevSliderValue) {
                updateForecast();
            }
        });
        
        forecastPanel = new VBox(fcDaysBar, fcHoursBar, fcSlider);
    }
    
    /**
     * Placeholder for the History panel
     */
    private void buildHistoryPanel() {
        historyPanel = new VBox();
        historyPanel.getChildren().add(new Label("History"));
    }
    
    /**
     * Builds the Maps panel.
     * This is an option for the bottom half of the main window.
     */
    private void buildMapsPanel() {
        // Initialization
        WeatherMap.init();
        
        // Main view of map
        mapImage = new ImageView();
        mapImage.setPreserveRatio(true);
        mapImage.setFitHeight(mapHeight);
        mapImageBase = new ImageView();
        mapImageBase.setPreserveRatio(true);
        mapImageBase.setFitHeight(mapHeight);

        var mapContainer = new StackPane();
        mapContainer.getChildren().addAll(mapImageBase, mapImage);
        mapContainer.setPadding(new Insets(25, 40, 25, 40));
        
        // Controls for moving and zooming
        var moveButtonRight = new Button("->");
        moveButtonRight.setOnAction((event) -> {
            WeatherMap.moveRight();
            updateMaps();
        });
        moveButtonRight.setTextAlignment(TextAlignment.CENTER);
        moveButtonRight.setMinSize(moveButtonLength, moveButtonWidth);
        var moveButtonDown = new Button("|\nv");
        moveButtonDown.setOnAction((event) -> {
            WeatherMap.moveDown();
            updateMaps();
        });
        moveButtonDown.setTextAlignment(TextAlignment.CENTER);
        moveButtonDown.setMinSize(moveButtonWidth, moveButtonLength);
        var moveButtonLeft = new Button("<-");
        moveButtonLeft.setOnAction((event) -> {
            WeatherMap.moveLeft();
            updateMaps();
        });
        moveButtonLeft.setTextAlignment(TextAlignment.CENTER);
        moveButtonLeft.setMinSize(moveButtonLength, moveButtonWidth);
        var moveButtonUp = new Button("^\n|");
        moveButtonUp.setOnAction((event) -> {
            WeatherMap.moveUp();
            updateMaps();
        });
        moveButtonUp.setTextAlignment(TextAlignment.CENTER);
        moveButtonUp.setMinSize(moveButtonWidth, moveButtonLength);
        var zoomButtonIn = new Button("+");
        var zoomButtonOut = new Button("-");
        zoomButtonIn.setOnAction((event) -> {
            // If zoom in allowed, zoom in
            if (WeatherMap.canZoomIn()) WeatherMap.zoomIn();
            
            // Disable zoom in button if cannot zoom in
            zoomButtonIn.setDisable(!WeatherMap.canZoomIn());
            
            // Enable zoom out button, as we have just zoomed in
            zoomButtonOut.setDisable(false);
            
            // Update graphics
            updateMaps();
        });
        zoomButtonOut.setOnAction((event) -> {
            // If zoom out allowed, zoom out
            if (WeatherMap.canZoomOut()) WeatherMap.zoomOut();
            
            // Disable zoom out button if cannot zoom out
            zoomButtonOut.setDisable(!WeatherMap.canZoomOut());
            
            // Enable zoom in button, as we have just zoomed out
            zoomButtonIn.setDisable(false);
            
            // Update graphics
            updateMaps();
        });
        zoomButtonIn.setTextAlignment(TextAlignment.CENTER);
        zoomButtonOut.setTextAlignment(TextAlignment.CENTER);
        zoomButtonIn.setMinSize(moveButtonLength, moveButtonLength / 2);
        zoomButtonOut.setMinSize(moveButtonLength, moveButtonLength / 2);
        zoomButtonIn.setDisable(!WeatherMap.canZoomIn());
        zoomButtonOut.setDisable(!WeatherMap.canZoomOut());
        var zoomButtons = new VBox(zoomButtonIn, zoomButtonOut);
        zoomButtons.setAlignment(Pos.CENTER);
        var moveButtonContainer = new GridPane();
        moveButtonContainer.add(moveButtonUp, 1, 0);
        moveButtonContainer.add(moveButtonLeft, 0, 1);
        moveButtonContainer.add(zoomButtons, 1, 1);
        moveButtonContainer.add(moveButtonRight, 2, 1);
        moveButtonContainer.add(moveButtonDown, 1, 2);
        moveButtonContainer.setPrefSize(3*moveButtonLength, 3*moveButtonLength);
        var moveButtonPlacer = new VBox(moveButtonContainer);
        moveButtonPlacer.setAlignment(Pos.CENTER);
        
        // Horizontal layout for map and buttons due to limited space
        var hLayout = new HBox(mapContainer, moveButtonPlacer);
        
        // Controls for map mode
        var modeButtonRain = new Button("Rain");
        modeButtonRain.setOnAction((event) -> {
            WeatherMap.setMode("Rain");
            updateMaps();
        });
        var modeButtonTemp = new Button("Temperature");
        modeButtonTemp.setOnAction((event) -> {
            WeatherMap.setMode("Temperature");
            updateMaps();
        });
        var modeButtonWind = new Button("Wind");
        modeButtonWind.setOnAction((event) -> {
            WeatherMap.setMode("Wind");
            updateMaps();
        });
        var modeButtonClouds = new Button("Clouds");
        modeButtonClouds.setOnAction((event) -> {
            WeatherMap.setMode("Clouds");
            updateMaps();
        });
        var modeButtonContainer = new HBox(modeButtonRain, modeButtonTemp,
                modeButtonWind, modeButtonClouds);
        
        mapsPanel = new VBox(hLayout, modeButtonContainer);
    }
    
    /**
     * Builds one index of the graphical search history in the Search window.
     * @param offset Used to place the element correctly in the GUI
     * @param index The index of the element in the UI arrays
     */
    private void buildSearchHistory(int offset, int index) {
        var nameField = new Label();
        nameField.setPrefWidth(searchTextWidth);
        nameField.setPadding(new Insets(0, 0, 0, 10));
        var selectButton = new Button("Select");
        selectButton.setOnAction((event) -> {
            api.setLocationActive(nameField.getText());
            update();
        });
        selectButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        var favoriteButton = new Button("Favorite");
        favoriteButton.setOnAction((event) -> {
            api.addToFavorites(nameField.getText());
            update();
        });
        favoriteButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // Make elemenets invisible by default
        selectButton.setVisible(false);
        favoriteButton.setVisible(false);
        
        // Add elements to UI
        int gridIndex = index + offset;
        searchLayout.add(nameField, 0, gridIndex);
        searchLayout.add(selectButton, 1, gridIndex);
        searchLayout.add(favoriteButton, 2, gridIndex);
        
        // Add elements to arrays
        arrayHistoryName[index] = nameField;
        arrayHistorySelect[index] = selectButton;
        arrayHistoryFavorite[index] = favoriteButton;
    }
    
    /**
     * Builds one index of the graphical favorites list in the Search window.
     * @param offset Used to place the element correctly in the GUI
     * @param index The index of the element in the UI arrays
     */
    private void buildSearchFavorite(int offset, int index) {
        var nameField = new Label();
        nameField.setPrefWidth(searchTextWidth);
        nameField.setPadding(new Insets(0, 0, 0, 10));
        var selectButton = new Button("Select");
        selectButton.setOnAction((event) -> {
            api.setLocationActive(nameField.getText());
            update();
        });
        selectButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        var deleteButton = new Button("Unfavorite");
        deleteButton.setOnAction((event) -> {
            api.removeFromFavorites(nameField.getText());
            searchLayout.requestFocus(); // Remove focus from delete button
            update();
        });
        deleteButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // Make elemenets invisible by default
        selectButton.setVisible(false);
        deleteButton.setVisible(false);
        
        // Add elements to UI
        int gridIndex = index + offset;
        searchLayout.add(nameField, 0, gridIndex);
        searchLayout.add(selectButton, 1, gridIndex);
        searchLayout.add(deleteButton, 2, gridIndex);
        
        // Add elements to arrays
        arrayFavoriteName[index] = nameField;
        arrayFavoriteSelect[index] = selectButton;
        arrayFavoriteDelete[index] = deleteButton;
    }
    
    /**
     * Performs a query through the API with the given location.
     * Restores the previous location if the query was unsuccessful.
     * @param location The name of the location in String format
     */
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
    
    /**
     * Builds the separate Search window.
     */
    private void buildSearchWindow() {
        // Vertical main layout
        int row = 0;
        searchLayout = new GridPane();
        searchWindow = new Stage();
        searchWindow.setScene(new Scene(searchLayout));
        
        // Label and close button at the top
        var searchLabel = new Label("Search");
        searchLabel.setFont(new Font("C059 Bold", 24));
        searchLabel.setPrefWidth(searchTextWidth);
        searchLabel.setAlignment(Pos.CENTER_LEFT);
        searchLabel.setPadding(new Insets(0, 0, 0, 10));
        var closeButton = new Button("Close"); // Button for closing window
        closeButton.setOnAction((event) -> {searchWindow.close();});
        closeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchLayout.add(searchLabel, 0, row);
        searchLayout.add(closeButton, 2, row);
        row++;
        
        // Search status field
        searchStatusField = new Label();
        searchStatusField.setPadding(new Insets(5, 0, 5, 10));
        searchLayout.add(searchStatusField, 0, row);
        row++;
        
        // Search row
        searchField = new TextField(); // Enter text
        searchField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) { // Start search
                search(searchField.getText());
            } else {
                searchStatusField.setText("");
            }
        });
        searchField.setPrefWidth(searchTextWidth);
        var searchButton = new Button("Search"); // Start search
        searchButton.setOnAction((event) -> {search(searchField.getText());});
        searchButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        var clearButton = new Button("Clear"); // Button for clearing search
        clearButton.setOnAction((event) -> {searchField.setText("");});
        clearButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchLayout.add(searchField, 0, row);
        searchLayout.add(searchButton, 1, row);
        searchLayout.add(clearButton, 2, row);
        row++;
        
        // Search history
        var historyLabel = new Label("History");
        historyLabel.setFont(new Font("C059 Bold", 24));
        historyLabel.setPrefWidth(searchTextWidth);
        historyLabel.setAlignment(Pos.CENTER_LEFT);
        historyLabel.setPadding(new Insets(0, 0, 0, 10));
        searchLayout.add(historyLabel, 0, row);
        row++;
        arrayHistoryName = new Label[maxHistory];
        arrayHistorySelect = new Button[maxHistory];
        arrayHistoryFavorite = new Button[maxHistory];
        for (int index = 0; index < maxHistory; index++) {
            buildSearchHistory(row, index);
        }
        row += 10;
        
        // Favorites
        var favoritesLabel = new Label("Favorites");
        favoritesLabel.setFont(new Font("C059 Bold", 24));
        favoritesLabel.setPrefWidth(searchTextWidth);
        favoritesLabel.setAlignment(Pos.CENTER_LEFT);
        favoritesLabel.setPadding(new Insets(0, 0, 0, 10));
        searchLayout.add(favoritesLabel, 0, row);
        row++;
        arrayFavoriteName = new Label[maxFavorites];
        arrayFavoriteSelect = new Button[maxFavorites];
        arrayFavoriteDelete = new Button[maxFavorites];
        for (int index = 0; index < maxFavorites; index++) {
            buildSearchFavorite(row, index);
        }
    }
    
    /**
     * Main initialization function.
     * Builds all windows and elements of the GUI.
     */
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
        forecastButton.setOnAction((event) -> {selectPanel(forecastPanel);});
        var historyButton = new Button("History");
        historyButton.setOnAction((event) -> {selectPanel(historyPanel);});
        var mapsButton = new Button("Maps");
        mapsButton.setOnAction((event) -> {selectPanel(mapsPanel);});
        midBar = new HBox(forecastButton, historyButton, mapsButton);
        mainLayout.getChildren().add(midBar);
        
        // Bottom part of window, initially Forecast panel
        buildForecastPanel();
        buildHistoryPanel();
        buildMapsPanel();
        basePanel = new StackPane(forecastPanel, historyPanel, mapsPanel);
        mainLayout.getChildren().add(basePanel);
        selectPanel(forecastPanel);
        
        // Focus today by default
        selectDay(0);
        
        // "Search & Favorites" window
        buildSearchWindow();
    }
    
    /**
     * Fetches an icon matching the given identifier and handles exceptions.
     * @param icon The filename (without extension) of the requested icon
     * @return Image The requested icon, default icon or null if not available
     */
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
    
    /**
     * Updates the main weather panel at the top of the window.
     */
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
        double rainValue = weather.getRain().get1h();
        String rain = String.format(Locale.US, "%.1f", rainValue);
        currentRainField.setText(rain);
        currentRainUnitField.setText(api.getUnitRain());
        currentWindIcon.setImage(getIcon("wind"));
        currentWindField.setText(weather.getWind().getSpeed());
        currentWindUnitField.setText(api.getUnitWind());
    }
    
    /**
     * Updates the bar graph representing rain in the Forecast panel.
     * @param dayIndex The index of the day the rain data is related to
     */
    void updateRainMeter(int dayIndex) {
        var forecast = api.getForecastHourly().getList();
        int currentHour = Integer.parseInt(forecast.get(0).getHour());
        for (int hourIndex = 0; hourIndex < 24; hourIndex++) {
            String style;
            int forecastIndex = 24 * dayIndex + hourIndex - currentHour;
            if ((forecastIndex > 0) & (forecastIndex < forecast.size())) {
                var forecastHour = forecast.get(forecastIndex);
                double rainValue = forecastHour.getRain().get1h();
                String unitSystem = api.getUnit();
                if (unitSystem.equals("Metric")) { // Rain measured in mm
                    // Check rain intensity
                    if (rainValue == 0.0) { // No rain
                        style = styleRain[0];
                    } else if (rainValue < 2.5) { // Light rain
                        style = styleRain[1];
                    } else if (rainValue < 10) { // Moderate rain
                        style = styleRain[2];
                    } else if (rainValue < 50) { // Heavy rain
                        style = styleRain[3];
                    } else { // Violent rain
                        style = styleRain[4];
                    }
                } else { // Rain (probably) measured in inches
                    // Check rain intensity
                    if (rainValue == 0.0) { // No rain
                        style = styleRain[0];
                    } else if (rainValue < 0.098) { // Light rain
                        style = styleRain[1];
                    } else if (rainValue < 0.39) { // Moderate rain
                        style = styleRain[2];
                    } else if (rainValue < 2.0) { // Heavy rain
                        style = styleRain[3];
                    } else { // Violent rain
                        style = styleRain[4];
                    }
                }
            } else {
                style = styleNoData;
            }
            arrayRainMeters[dayIndex][hourIndex].setStyle(style);
        }
    }
    
    /**
     * Updates one day of the daily forecast in the Forecast panel.
     * @param index The index of the UI elements corresponding to the day
     */
    private void updateForecastDay(int index) {
        var forecastDay = api.getForecastDaily().getList().get(index);
        Image icon = getIcon(forecastDay.getWeather().get(0).getIcon());
        arrayDayIcon[index].setImage(icon);
        arrayDayWeekday[index].setText(forecastDay.getWeekday());
        arrayDayDate[index].setText(forecastDay.getDate());
        arrayDayTempMin[index].setText(forecastDay.getTemp().getMin());
        arrayDayTempMax[index].setText(forecastDay.getTemp().getMax());
        arrayDayTempUnit[index].setText(api.getUnitTemp());
        updateRainMeter(index);
    }
    
    /**
     * Updates one hour of the hourly forecast in the Forecast panel.
     * @param bias Pans the 16-hour selection window
     * @param index The index of the UI elements corresponding to the hour
     */
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
            double rainValue = forecastHour.getRain().get1h();
            String rainString = String.format(Locale.US, "%.1f", rainValue);
            arrayHourRainStat[index].setText(rainString);
            arrayHourRainPerc[index].setText(forecastHour.getPop());
            arrayHours[index].setVisible(true);
        } else { // Forecast doesn't go this far (forward or back)
            arrayHours[index].setVisible(false);
        }
    }
    
    /**
     * Updates the Forecast panel.
     */
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
            fcSlider.setDisable(true);
            if (currentHour < buffer) {
                biasHours = currentHour;
            } else {
                biasHours = buffer;
            }
        } else if ((selectedDay >= forecastDays - 1) & (currentHour > buffer)) {
            fcSlider.setDisable(true);
        } else {
            fcSlider.setDisable(false);
        }
        int bias = biasDays + biasHours - currentHour;
        for (int i = 0; i < forecastHours; i++) {
            updateForecastHour(bias, i);
        }
    }
    
    /**
     * Updates the Maps panel.
     */
    private void updateMaps() {
        Image mapTextureWeather;
        Image mapTextureBase;
        
        boolean validWeather = true;
        boolean validBase = true;
        
        try {
            final InputStream mapFile = new DataInputStream(
                    new FileInputStream(WeatherMap.getFileName()));
            mapTextureWeather = new Image(mapFile);
        } catch (IOException exception) {
            mapTextureWeather = null;
            validWeather = false;
        }
        try {
            mapTextureBase = new Image(
                    new FileInputStream(WeatherMap.getBaseFilename()));            
        } catch (IOException exception) {
            mapTextureBase = null;
            validBase = false;
        }
        if(!validWeather && !validBase) {
            mapTextureBase = getIcon("default");
            mapTextureWeather = getIcon("default");
        }
        
        mapImageBase.setImage(mapTextureBase);
        mapImage.setImage(mapTextureWeather);
        mapImage.setOpacity(0.7);
    }
    
    /**
     * Updates the list of search history entries in the search window.
     */
    private void updateSearchHistory() {
        int size = api.getLocationHistory().size();
        for (int i = 0; i < maxHistory; i++) {
            if (i < size) {
                arrayHistoryName[i].setText(api.getLocationHistory().get(i));
                arrayHistorySelect[i].setVisible(true);
                arrayHistoryFavorite[i].setVisible(true);
            } else {
                arrayHistoryName[i].setText("");
                arrayHistorySelect[i].setVisible(false);
                arrayHistoryFavorite[i].setVisible(false);
            }
        }
    }
    
    /**
     * Updates the list of favorites in the search window.
     */
    private void updateSearchFavorites() {
        int size = api.getLocationFavorites().size();
        for (int i = 0; i < maxFavorites; i++) {
            if (i < size) {
                arrayFavoriteName[i].setText(api.getLocationFavorites().get(i));
                arrayFavoriteSelect[i].setVisible(true);
                arrayFavoriteDelete[i].setVisible(true);
            } else {
                arrayFavoriteName[i].setText("");
                arrayFavoriteSelect[i].setVisible(false);
                arrayFavoriteDelete[i].setVisible(false);
            }
        }
    }
    
    /**
     * Main update function
     * Updates all fields in all windows of the GUI with new data from the API.
     * @return boolean true if the api request was successful
     */
    private boolean update() {
        // Request weather and forecast data from api
        boolean requestSuccess = api.getData();
        if (requestSuccess) {
            // Update main window UI fields
            cityLabel.setText(api.getLocationActive());
            updateWeatherPanel();
        
            // Update bottom panel
            updateForecast();
            updateMaps();
        
            // Update search window UI fields
            searchStatusField.setText("");
            updateSearchHistory();
            updateSearchFavorites();
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