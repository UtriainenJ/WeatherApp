package fi.tuni.prog3.weatherapp;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.Console;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({WeatherAPI.class})
public class WeatherAPITest {

    private WeatherAPI wAPI;
    private String wData1;
    private String wData2;
    private String APIkey;





    @Before
    public void setUp() throws Exception{
        wAPI = new WeatherAPI();

        wData1 = "{\"coord\":{\"lon\":10.99,\"lat\":44.34},\"weather\":[{\"id\":501,\"main\":\"Rain\"," +
                "\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\"" +
                ":298.48,\"feels_like\":298.74,\"temp_min\":297.56,\"temp_max\":300.05,\"pressure\":1015,\"humidity" +
                "\":64,\"sea_level\":1015,\"grnd_level\":933},\"visibility\":10000,\"wind\":{\"speed\":0.62,\"deg\"" +
                ":349,\"gust\":1.18},\"rain\":{\"1h\":3.16},\"clouds\":{\"all\":100},\"dt\":1661870592,\"sys\":{\"" +
                "type\":2,\"id\":2075663,\"country\":\"IT\",\"sunrise\":1661834187,\"sunset\":1661882248},\"timezone" +
                "\":7200,\"id\":3163858,\"name\":\"Zocca\",\"cod\":200}";

        wData2 = "{\"coord\":{\"lon\":11.99,\"lat\":45.34},\"weather\":[{\"id\":501,\"main\":\"Rain\"," +
                "\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\"" +
                ":298.48,\"feels_like\":298.74,\"temp_min\":297.56,\"temp_max\":303.05,\"pressure\":1015,\"humidity" +
                "\":64,\"sea_level\":1015,\"grnd_level\":933},\"visibility\":10000,\"wind\":{\"speed\":0.62,\"deg\"" +
                ":349,\"gust\":1.18},\"rain\":{\"1h\":3.16},\"clouds\":{\"all\":100},\"dt\":1661870592,\"sys\":{\"" +
                "type\":2,\"id\":2075663,\"country\":\"IT\",\"sunrise\":1661834187,\"sunset\":1661882248},\"timezone" +
                "\":7200,\"id\":3163858,\"name\":\"Mocca\",\"cod\":200}";

        APIkey = "11";
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
    public void testGetCurrentWeather() throws Exception {


        String url1 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Zocca" + "&appid=" + APIkey + "&units=" + "null";
        String url2 = "https://pro.openweathermap.org/data/2.5/weather"
                + "?q=" + "Mocca" + "&appid=" + APIkey + "&units=" + "null";


        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAPIKey"))
                .with((proxy, method, args) -> APIkey); // only args is actually needed

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "makeHTTPCall", String.class))
                .with((proxy, method, args) -> {
                    String url = (String) args[0];

                    if (url.equals(url1)) {
                        return wData1;
                    } else if (url.equals(url2)) {
                        return wData2;
                    }
                    return "{}";
                });

        PowerMockito.replace(PowerMockito.method(WeatherAPI.class, "getAirQuality", WeatherData.class))
                .with((proxy, method, args) -> "Poor");

        assertEquals("Zocca",wAPI.getCurrentWeather("Zocca").getName());
        assertEquals("Poor",wAPI.getCurrentWeather("Zocca").getAirQuality());

        assertEquals("Mocca",wAPI.getCurrentWeather("Mocca").getName());

        assertNull("Should be null with invalid location",wAPI.getCurrentWeather("Invalid").getName());
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







