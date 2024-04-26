package fi.tuni.prog3.weatherapp;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.Before;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Unit tests for WeatherAPI using Junit4 framework
 * Data from openweather API is mocked using PowerMockito
 * @author Jaakko
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WeatherAPI.class})
public class WeatherAPITest {

    private WeatherAPI wAPI;
    private String cData1;
    private String cData2;
    private String hData1;
    private String hData2;
    private String APIkey;
    private String locData;
    private String dData;
    private String aData;


    /**
     * Reads data from mockDat.json and places them in attributes in addition to initializing an instance of WeatherAPI
     */
    @Before
    public void setUp() throws Exception{
        wAPI = new WeatherAPI();

        // in @before instead of @beforeclass due to powermock limitations
        String mockDataPath = "src/test/java/fi/tuni/prog3/weatherapp/mockData.json";
        cData1 = FileUtil.readJsonLines(mockDataPath).get(0);
        cData2 = FileUtil.readJsonLines(mockDataPath).get(1);
        hData1 = FileUtil.readJsonLines(mockDataPath).get(2);
        hData2 = FileUtil.readJsonLines(mockDataPath).get(3);
        locData = FileUtil.readJsonLines(mockDataPath).get(4);
        dData = FileUtil.readJsonLines(mockDataPath).get(5);
        aData = FileUtil.readJsonLines(mockDataPath).get(6);

        APIkey = "11";

        mockGetAPIKey();
    }

    /**
     * Mocks the API response by checking if the url is correct and returning corresponding data in json format
     * @param url1 first url option
     * @param url2 second url option
     * @param data1 first data option
     * @param data2 second data option
     */
    private void mockHTTPCall(String url1, String url2, String data1, String data2){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class,
                "makeHTTPCall", String.class)).with((proxy, method, args) -> {
                    String url = (String) args[0];

                    if (url.equals(url1)) {
                        return data1;
                    } else if (url.equals(url2)) {
                        return data2;
                    }
                    else
                        throw new IllegalArgumentException("Error getting api"
                                + "response.");
                });
    }

    /**
     * Mocks the API response by checking if the url is correct and returning corresponding data in json format
     * single url version
     * @param url first url option
     * @param data first data option
     */
    private void mockHTTPCall(String url, String data){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class,
                "makeHTTPCall", String.class)).with((proxy, method, args) -> {
                    if (url.equals((String) args[0])) {
                        return data;
                    }
                    else
                        throw new IllegalArgumentException("Error getting api"
                                + "response.");
                });
    }


    /**
     * Replaces getAPIKey() and returns the testing key
     */
    private void mockGetAPIKey(){
        // only args is actually needed
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAPIKey"))
                .with((proxy, method, args) -> APIkey);
    }


    /**
     * Tests if the weatherAPI gets initialized properly
     */
    @Test
    public void testWeatherAPIInitialization(){
        assertNotNull("locationActive should never be null",
                wAPI.getLocationActive());

        assertNotNull("locationFavorites should be an instance of List",
                wAPI.getLocationFavorites());
        assertTrue("locationFavorites should be empty",
                wAPI.getLocationFavorites().isEmpty());

        assertNotNull("locationHistory should be an instance of List",
                wAPI.getLocationHistory());
        assertTrue("locationHistory should be empty",
                wAPI.getLocationHistory().isEmpty());
    }

    /**
     * Tests setLocationActive and getLocationActive
     */
    @Test
    public void testGetLocationActive(){
        wAPI.setLocationActive("tanpere");
        String expResult = "tanpere";
        String result = wAPI.getLocationActive();
        assertEquals("getLocationActive should return '" + expResult
                + "' but returned '" + result + "' instead", expResult, result);
    }


    /**
     * Tests addToFavorites and getLocationFavorites
     */
    @Test
    public void testGetLocationFavorites() {
        wAPI.addToFavorites("Helsinki");
        wAPI.addToFavorites("Espoo");

        List<String> expResult = Arrays.asList("Espoo", "Helsinki");
        List<String> result = wAPI.getLocationFavorites();

        assertEquals("getLocationFavorites should return '" + expResult
                + "' but returned '" + result + "' instead", expResult, result);
    }

    /**
     * Tests addToHistory and getLocationHistory
     */
    @Test
    public void testGetLocationHistory() {
        wAPI.addToHistory("Tampere");
        wAPI.addToHistory("Turku");

        List<String> expResult = Arrays.asList("Turku", "Tampere");
        List<String> result = wAPI.getLocationHistory();

        assertEquals("getLocationHistory should return '" + expResult
                + "' but returned '" + result + "' instead", expResult, result);
    }

    /**
     * Tests clearHistory
     */
    @Test
    public void testClearHistory(){
        wAPI.setLocationActive("Helsinki");
        wAPI.setLocationActive("Turku");
        wAPI.setLocationActive("tanpere");

        wAPI.clearBrowsingHistory();
        assertTrue("locationHistory should be empty after clearHistory,"
                + "instead it contained '" + wAPI.getLocationHistory() + "'",
                wAPI.getLocationHistory().isEmpty());
    }


    /**
     * Simulates the API call with mockHTTPCall and tests getCurrentWeather and makeWeatherObject indirectly
     */
    @Test
    public void testGetCurrentWeather(){
        String url1 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Zocca" + "&appid=" + APIkey + "&units=metric";
        String url2 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Sant'Angelo di Piove di Sacco" + "&appid=" + APIkey
                + "&units=metric";

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class,
                "getAirQuality", WeatherData.class))
                .with((proxy, method, args) -> "Poor");

        mockHTTPCall(url1, url2, cData1, cData2);

        assertEquals("Zocca", wAPI.getCurrentWeather("Zocca").getName());
        assertEquals("Poor", wAPI.getCurrentWeather("Zocca").getAirQuality());
        assertEquals("Sant'Angelo di Piove di Sacco",
                wAPI.getCurrentWeather("Sant'Angelo di Piove di Sacco").getName());
        assertThrows(IllegalArgumentException.class,
                () -> wAPI.getCurrentWeather("Kempele"));
    }

    /**
     * Makes sure that mockHTTPCall simulates APIerrors correctly and tests said errors in getCurrentWeather
     */
    @Test
    public void testAPIError(){
        mockHTTPCall("http://www.yle.fi/~ransu", cData1);
        assertThrows(IllegalArgumentException.class,
                () -> wAPI.getCurrentWeather("Zocca"));

        mockHTTPCall("www.abc.de", "www.efg.ij", hData1, hData2);
        assertThrows(IllegalArgumentException.class,
                () -> wAPI.getCurrentWeather("Zocca"));
    }

    /**
     * Simulates the API call with mockHTTPCall and tests getCurrentWeather and makeWeatherObject indirectly
     */
    @Test
    public void testGetCurrentWeatherCoords(){
        double lon1 = 10.99;
        double lat1 = 44.34;
        double lon2 = 11.99;
        double lat2 = 45.34;

        String url1 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?lat=" + String.format(Locale.US,"%.4f",lat1) + "&lon="
                + String.format(Locale.US,"%.4f",lon1) + "&appid=" + APIkey
                +"&units=metric";

        String url2 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?lat=" + String.format(Locale.US,"%.4f",lat2) + "&lon="
                + String.format(Locale.US,"%.4f",lon2) + "&appid=" + APIkey
                + "&units=metric";

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class,
                "getAirQuality", WeatherData.class))
                .with((proxy, method, args) -> "Poor");

        mockHTTPCall(url1, url2, cData1, cData2);

        assertEquals("Zocca",wAPI.getCurrentWeather(lat1,lon1 ).getName());
        assertEquals("Poor",wAPI.getCurrentWeather(lat1,lon1).getAirQuality());
        assertEquals("Sant'Angelo di Piove di Sacco",
                wAPI.getCurrentWeather(lat2,lon2).getName());
        assertThrows(IllegalArgumentException.class,
                () -> wAPI.getCurrentWeather(10,13));
    }

    /**
     * Simulates the API call with mockHTTPCall and tests getCurrentWeather and makeWeatherObject indirectly
     */
    @Test
    public void testGetCurrentWeatherInvalidCoords(){
        double lat = 91.0; // Invalid latitude
        double lon = -74.0060; // Valid longitude

        assertThrows("Should throw an exception with invalid coords",
                IllegalStateException.class, () ->
                        wAPI.getCurrentWeather(lat,lon));

        double lat2 = 90.0; // Edge case
        double lon2 = -180.0060;

        assertThrows("Should throw an exception with invalid coords",
                IllegalStateException.class, () ->
                        wAPI.getCurrentWeather(lat2, lon2));
    }

    /**
     * Simulates the API call with mockHTTPCall and tests getForecastHourly and makeForecastHourlyObject indirectly
     */
    @Test
    public void testGetForecastHourlyCoords(){
        double lon1 = 10.99;
        double lat1 = 44.34;
        double lon2 = 11.99;
        double lat2 = 45.34;

        String url1 = "https://pro.openweathermap.org/data/2.5/forecast/hourly"
                    + "?lat=" + String.format(Locale.US,"%.4f",lat1) + "&lon=" +
                String.format(Locale.US,"%.4f",lon1) + "&appid=" + APIkey + "&units=metric";

        String url2 = "https://pro.openweathermap.org/data/2.5/forecast/hourly"
                + "?lat=" + String.format(Locale.US,"%.4f",lat2) + "&lon=" +
                String.format(Locale.US,"%.4f",lon2) + "&appid=" + APIkey + "&units=metric";

        mockHTTPCall(url1, url2, hData1, hData2);

        ForecastDataHourly.WeatherEntry result1 = wAPI.getForecastHourly(lat1, lon1).getList().get(0);
        ForecastDataHourly.WeatherEntry result2 = wAPI.getForecastHourly(lat2, lon2).getList().get(0);

        assertEquals("3", result1.getMain().getTemp());
        assertEquals("1011", result1.getMain().getSea_level());
        assertEquals("9", result2.getMain().getTemp());
    }

    /**
     * Simulates the API call with mockHTTPCall and tests getForecastHourly and makeForecastHourlyObject indirectly
     */
    @Test
    public void testGetForecastHourlyInvalidCoords(){
        double lat1 = 91.0; // Invalid latitude
        double lon1 = -74.0060; // Valid longitude

        assertThrows("Should throw an exception with invalid coords", IllegalStateException.class,
                () -> wAPI.getForecastHourly(lat1,lon1));

        double lat2 = 90.0; // Edge case
        double lon2 = -180.0060;

        assertThrows("Should throw an exception with invalid coords", IllegalStateException.class,
                () -> wAPI.getForecastHourly(lat2, lon2));
    }

    /**
     * Indirectly tests private getLocation method
     */
    @Test
    public void testGetLocation(){
        String loc = "Zocca";

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getLocation"))
                .with((proxy, method, args) -> (new Pair<>(44.3472,10.9904)));

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getForecastHourly",
                        double.class, double.class)).with((proxy, method, args) -> {
                            double lat = (double)args[0];
                            double lon = (double)args[1];

                            //checks if coordinates fetched correctly
                            assertEquals(44.3472,lat, 0.001);
                            assertEquals(10.9904, lon, 0.001);
                            return new ForecastDataHourly();
                        });

        wAPI.getForecastHourly(loc);
    }

    /**
     * Test getAirQuality
     */
    @Test
    public void testGetAirQuality(){
        double lat = 44.3472;
        double lon = 10.9904;
        String url = "http://api.openweathermap.org/data/2.5/air_pollution"
                + "?lat=" + String.format(Locale.US,"%.4f",lat) + "&lon=" +
                String.format(Locale.US,"%.4f",lon) + "&appid=" + APIkey;

        mockHTTPCall(url, aData);
    }

    /**
     * Simulates the API call with mockHTTPCall and tests getForecastDaily and makeForecastDailyObject indirectly
     */
    @Test
    public void testGetForecastDaily(){
        String loc = "Zocca";

        String url = "https://api.openweathermap.org/data/2.5/forecast/daily"
                + "?lat=" + 44.3472 + "&lon=" + 10.9904
                + "&appid=" + APIkey + "&units=metric";

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getLocation"))
                .with((proxy, method, args) -> (new Pair<>(44.3472,10.9904)));

        mockHTTPCall(url,dData);

        ForecastDataDaily.WeatherEntry result = wAPI.getForecastDaily(loc).getList().get(0);
        assertNotNull(result);
        assertEquals("26.4", result.getDate());
        assertEquals("3", result.getGust());
    }

    /**
     * Creates weather and forecast objects and fills them with mock data, then tests getData() and finally makes sure
     * that unit Methods work correctly as well
     */
    @Test
    public void testUnits(){
        wAPI.setLocationActive("Zocca");

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getLocation"))
                .with((proxy, method, args) -> (new Pair<>(44.3472,10.9904)));
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAirQuality", WeatherData.class))
                .with((proxy, method, args) -> "Poor");

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "makeHTTPCall", String.class))
                .with((proxy, method, args) -> {
                    String url = (String) args[0];

                    if (url.contains("weather")) {
                        return cData1;
                    } else if (url.contains("hourly")) {
                        return hData1;
                    }
                    else if (url.contains("daily")){
                        return dData;
                    }
                    else
                        throw new IllegalArgumentException("Error getting api response.");
                });

        wAPI.getData();

        wAPI.getWeather().setUnits("m");
        wAPI.getForecastDaily().setUnits("m");
        wAPI.getForecastHourly().setUnits("m");
        assertNotNull(wAPI.getWeather());
        assertNotNull(wAPI.getForecastDaily());
        assertNotNull(wAPI.getForecastHourly());


        wAPI.setUnits("m");
        assertEquals("Unit should be 'Metric', instead was " + wAPI.getUnit(),
                "Metric" , wAPI.getUnit());

        wAPI.setUnits("I");
        assertEquals("Unit should be 'Imperial', instead was " + wAPI.getUnit(),
                "Imperial" , wAPI.getUnit());

        wAPI.switchUnits();
        assertEquals("Unit should be 'Metric' after switching, instead was " + wAPI.getUnit(),
                "Metric" , wAPI.getUnit());
    }
}


/**
 * Utility class to read json data from mockData.json
 */

class FileUtil {

/**
 * Reads data from a json file
 * @param filePath path to mockData file
 */
    public static List<String> readJsonLines(String filePath) {
        List<String> jsonLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("//") || line.trim().startsWith("#")) {
                    continue; // Skip this line
                }
                jsonLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonLines;
    }
}



