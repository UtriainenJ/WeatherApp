package fi.tuni.prog3.weatherapp;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;



import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(Parameterized.class)
public class WeatherAPIParameterizedTest {


    private WeatherAPI wAPI;
    private  String wData1;
    private  String wData2;
    private  String APIkey;

    private String location;

    public WeatherAPIParameterizedTest(String location) {
        this.location = location;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Helsinki"}, {"Kouvola"}, {"tanpere"}, {null}
        });
    }


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
    public void testSetLocationActive(){
        String result = wAPI.setLocationActive(location).getLocationActive();
        assertEquals("setLocationActive followed by getLocationActive should return '" + location +
                "' but returned '" + result + "' instead", result, location);

    }



    @Test
    public void testAddToFavorites() {
        wAPI.addToFavorites(location);
        wAPI.addToFavorites(location); // Duplicate attempt
        assertEquals("addToFavorites should not add duplicate location '" + location + "'",
                1, wAPI.getLocationFavorites().size());
        assertTrue("addToFavorites should add '" + location + "' to the favorites list.",
                wAPI.getLocationFavorites().contains(location));
    }


    @Test
    public void testRemoveFromFavorites() {
        wAPI.addToFavorites(location);
        wAPI.removeFromFavorites(location);
        assertFalse("removeFromFavorites should remove '" + location + "' from the favorites list.",
                wAPI.getLocationFavorites().contains(location));

        //removing non-existent location
        wAPI.removeFromFavorites(location);
        assertEquals("removeFromFavorites should handle removing a non-existent location",
                0, wAPI.getLocationFavorites().size());
    }


    @Test
    public void testAddToHistory() {
        wAPI.setLocationActive(location);
        wAPI.setLocationActive(location);
        wAPI.setLocationActive(location); // Duplicate attempt
        assertEquals("addToHistory should not increase the list size when adding duplicate '" + location + "'",
                1, wAPI.getLocationHistory().size());
        assertEquals("addToHistory should move '" + location + "' to the front of the history list ",
                location, wAPI.getLocationHistory().get(0));
    }




}






