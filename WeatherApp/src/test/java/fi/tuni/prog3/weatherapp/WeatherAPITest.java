package fi.tuni.prog3.weatherapp;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.junit.runner.RunWith;


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



    @Before
    public void setUp() throws Exception{
        wAPI = new WeatherAPI();

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
        mockGetAirQuality(); // mock value used for testing other methods, airquality is tested separately
    }

    private void mockHTTPCall(String url1, String url2, String data1, String data2){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "makeHTTPCall", String.class))
                .with((proxy, method, args) -> {
                    String url = (String) args[0];

                    if (url.equals(url1)) {
                        return data1;
                    } else if (url.equals(url2)) {
                        return data2;
                    }
                    else
                        throw new IllegalArgumentException("Error getting api response.");
                });
    }
    private void mockHTTPCall(String url, String data){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "makeHTTPCall", String.class))
                .with((proxy, method, args) -> {
                    if (url.equals((String) args[0])) {
                        return data;
                    }
                    else
                        throw new IllegalArgumentException("Error getting api response.");
                });
    }


    private void mockGetAPIKey(){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAPIKey"))
                .with((proxy, method, args) -> APIkey); // only args is actually needed
    }

    private void mockGetAirQuality(){
        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAirQuality", WeatherData.class))
                .with((proxy, method, args) -> "Poor");
    }


    @Test
    public void testWeatherAPIInitialization(){
        assertNull("locationActive should be null", wAPI.getLocationActive());

        assertNotNull("locationFavorites should be an instance of List", wAPI.getLocationFavorites());
        assertTrue("locationFavorites should be empty", wAPI.getLocationFavorites().isEmpty());

        assertNotNull("locationHistory should be an instance of List", wAPI.getLocationHistory());
        assertTrue("locationHistory should be empty", wAPI.getLocationHistory().isEmpty());
    }

    @Test
    public void testGetLocationActive(){
        wAPI.setLocationActive("tanpere");
        String expResult = "tanpere";
        String result = wAPI.getLocationActive();
        assertEquals("getLocationActive should return '" + expResult + "' but returned '" + result + "' instead",
                expResult, result);
    }


    @Test
    public void testGetLocationFavorites() {
        wAPI.addToFavorites("Helsinki");
        wAPI.addToFavorites("Espoo");

        List<String> expResult = Arrays.asList("Espoo", "Helsinki");
        List<String> result = wAPI.getLocationFavorites();

        assertEquals("getLocationFavorites should return '" + expResult + "' but returned '" +
                result + "' instead", expResult, result);


    }


    /* temporarily commented out
    @Test
    public void testAddToFavoritesWithNull() {
        wAPI.addToFavorites(null);
        assertFalse("addToFavorites should not add null to the favorites list.",
                wAPI.getLocationFavorites().contains(null));
    }

     */

    @Test
    public void testGetLocationHistory() {
        wAPI.setLocationActive("Tampere");
        wAPI.setLocationActive("Turku");

        List<String> expResult = Arrays.asList("Turku", "Tampere");
        List<String> result = wAPI.getLocationHistory();

        assertEquals("getLocationHistory should return '" + expResult + "' but returned '" + result +
                "' instead", expResult, result);

    }

/* temporarily commented out
    @Test
    public void testSetLocationActiveWithNull() {
        wAPI.setLocationActive(null);
        assertFalse("setLocationActive should not add null to the history list.",
                wAPI.getLocationHistory().contains(null));
    }

 */

    @Test
    public void testClearHistory(){
        wAPI.setLocationActive("Helsinki");
        wAPI.setLocationActive("Turku");
        wAPI.setLocationActive("tanpere");

        wAPI.clearBrowsingHistory();
        assertTrue("locationHistory should be empty after clearHistory, instead it contained '"
                + wAPI.getLocationHistory() + "'", wAPI.getLocationHistory().isEmpty());
    }


    @Test
    public void testGetCurrentWeather(){
        String url1 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Zocca" + "&appid=" + APIkey + "&units=" + "null";
        String url2 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Sant'Angelo di Piove di Sacco" + "&appid=" + APIkey + "&units=" + "null";

        mockHTTPCall(url1, url2, cData1, cData2);

        assertEquals("Zocca",wAPI.getCurrentWeather("Zocca").getName());
        assertEquals("Poor",wAPI.getCurrentWeather("Zocca").getAirQuality());
        assertEquals("Sant'Angelo di Piove di Sacco",
                wAPI.getCurrentWeather("Sant'Angelo di Piove di Sacco").getName());
        assertThrows(IllegalArgumentException.class, () -> wAPI.getCurrentWeather("Kempele"));
    }

    @Test
    public void testAPIError(){
        mockHTTPCall("http://www.yle.fi/~ransu", cData1);
        assertThrows(IllegalArgumentException.class, () -> wAPI.getCurrentWeather("Zocca"));

        mockHTTPCall("www.abc.de", "www.efg.ij", hData1, hData2);
        assertThrows(IllegalArgumentException.class, () -> wAPI.getCurrentWeather("Zocca"));
    }


    @Test
    public void testGetCurrentWeatherCoords(){
        double lon1 = 10.99;
        double lat1 = 44.34;
        double lon2 = 11.99;
        double lat2 = 45.34;

        String url1 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?lat=" + String.format(Locale.US,"%.4f",lat1) + "&lon=" +
                String.format(Locale.US,"%.4f",lon1) + "&appid=" + APIkey + "&units=" + "null";

        String url2 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?lat=" + String.format(Locale.US,"%.4f",lat2) + "&lon=" +
                String.format(Locale.US,"%.4f",lon2) + "&appid=" + APIkey + "&units=" + "null";

        mockHTTPCall(url1, url2, cData1, cData2);

        assertEquals("Zocca",wAPI.getCurrentWeather(lat1,lon1 ).getName());
        assertEquals("Poor",wAPI.getCurrentWeather(lat1,lon1).getAirQuality());
        assertEquals("Sant'Angelo di Piove di Sacco", wAPI.getCurrentWeather(lat2,lon2).getName());
        assertThrows(IllegalArgumentException.class, () -> wAPI.getCurrentWeather(10,13));
    }
    @Test
    public void testGetCurrentWeatherInvalidCoords(){
        double lat = 91.0; // Invalid latitude
        double lon = -74.0060; // Valid longitude

        assertThrows("Should throw an exception with invalid coords", IllegalStateException.class,
                () -> wAPI.getCurrentWeather(lat,lon ));

        double lat2 = 90.0; // Edge case
        double lon2 = -180.0060;

        assertThrows("Should throw an exception with invalid coords", IllegalStateException.class,
                () -> wAPI.getCurrentWeather(lat2, lon2));
    }

    @Test
    public void testGetForecastHourlyCoords(){
        double lon1 = 10.99;
        double lat1 = 44.34;
        double lon2 = 11.99;
        double lat2 = 45.34;

        String url1 = "https://pro.openweathermap.org/data/2.5/forecast/hourly"
                    + "?lat=" + String.format(Locale.US,"%.4f",lat1) + "&lon=" +
                String.format(Locale.US,"%.4f",lon1) + "&appid=" + APIkey + "&units=null";

        String url2 = "https://pro.openweathermap.org/data/2.5/forecast/hourly"
                + "?lat=" + String.format(Locale.US,"%.4f",lat2) + "&lon=" +
                String.format(Locale.US,"%.4f",lon2) + "&appid=" + APIkey + "&units=null";

        mockHTTPCall(url1, url2, hData1, hData2);

        ForecastDataHourly.WeatherEntry result1 = wAPI.getForecastHourly(lat1, lon1).getList().get(0);
        ForecastDataHourly.WeatherEntry result2 = wAPI.getForecastHourly(lat2, lon2).getList().get(0);

        assertEquals("3", result1.getMain().getTemp());
        assertEquals("1011", result1.getMain().getSea_level());
        assertEquals("9", result2.getMain().getTemp());
    }
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

        ForecastDataHourly result = wAPI.getForecastHourly(loc);


    }

    @Test
    public void testGetAirQuality(){
        double lat = 44.3472;
        double lon = 10.9904;
        String url = "http://api.openweathermap.org/data/2.5/air_pollution"
                + "?lat=" + String.format(Locale.US,"%.4f",lat) + "&lon=" +
                String.format(Locale.US,"%.4f",lon) + "&appid=" + APIkey;

        mockHTTPCall(url, aData);


    }

    @Test
    public void testGetForecastDaily(){
        String loc = "Zocca";

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getLocation"))
                .with((proxy, method, args) -> (new Pair<>(44.3472,10.9904)));

        String url = "https://api.openweathermap.org/data/2.5/forecast/daily"
                + "?lat=" + 44.3472 + "&lon=" + 10.9904
                + "&appid=" + APIkey + "&units=null";

        mockHTTPCall(url,dData);

        ForecastDataDaily.WeatherEntry result = wAPI.getForecastDaily(loc).getList().get(0);
        assertNotNull(result);
        assertEquals("26.4", result.getDate());
        assertEquals("3", result.getGust());

    }


/*
    @Test
    public void testUnits(){
        assertThrows("getUnits with no units set should throw an error",
                NullPointerException.class,() -> wAPI.getUnit());
        wAPI.setUnits("abc");
        assertThrows("invalid argument for setUnits shouldn't set any units",
                IllegalStateException.class,() -> wAPI.getUnit());

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

*/

}



class FileUtil {

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



