package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherAPITest {

    private WeatherAPI wAPI;
    @BeforeEach
    public void setUp() throws Exception{
        wAPI = new WeatherAPI();
    }

    @Test
    public void testWeatherAPIInitialization(){
        assertNull(wAPI.getLocationActive(), "locationActive should be null");

        assertNotNull(wAPI.getLocationFavorites(), "locationFavorites should be an instance of List");
        assertTrue(wAPI.getLocationFavorites().isEmpty(), "locationFavorites should be empty");

        assertNotNull(wAPI.getLocationHistory(), "locationHistory should be an instance of List");
        assertTrue(wAPI.getLocationHistory().isEmpty(), "locationHistory should be empty");
    }

    @Test
    public void testGetLocationActive(){
        wAPI.setLocationActive("tanpere");
        String expResult = "tanpere";
        String result = wAPI.getLocationActive();
        assertEquals(expResult, result,
                "getLocationActive should return '" + expResult + "' but returned '" + result + "' instead");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Helsinki", "Kouvola", "tanpere"})
    @NullSource
    public void testSetLocationActive(String location){
        String result = wAPI.setLocationActive(location).getLocationActive();
        assertEquals(result, location,
                "setLocationActive followed by getLocationActive should return '" + location +
                        "' but returned '" + result + "' instead");

    }

    @Test
    public void testGetLocationFavorites() {
        wAPI.addToFavorites("Helsinki");
        wAPI.addToFavorites("Espoo");

        List<String> expResult = Arrays.asList("Espoo", "Helsinki");
        List<String> result = wAPI.getLocationFavorites();

        assertEquals(expResult, result,
                "getLocationFavorites should return '" + expResult + "' but returned '" + result + "' instead");
    }

    @Test
    public void testGetLocationHistory() {
        wAPI.setLocationActive("Tampere");
        wAPI.setLocationActive("Turku");

        List<String> expResult = Arrays.asList("Turku", "Tampere");
        List<String> result = wAPI.getLocationHistory();

        assertEquals(expResult, result,
                "getLocationHistory should return '" + expResult + "' but returned '" + result + "' instead");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Helsinki", "Kouvola", "tanpere"})
    public void testAddToFavorites(String location) {
        wAPI.addToFavorites(location);
        wAPI.addToFavorites(location); // Duplicate attempt
        assertEquals(1, wAPI.getLocationFavorites().size(),
                "addToFavorites should not add duplicate location '" + location + "'");
        assertTrue(wAPI.getLocationFavorites().contains(location),
                "addToFavorites should add '" + location + "' to the favorites list.");
    }

/*  temporarily commented out
    @Test
    public void testAddToFavoritesWithNull() {
        wAPI.addToFavorites(null);
        assertFalse(wAPI.getLocationFavorites().contains(null),
                "addToFavorites should not add null to the favorites list.");
    }

 */

    @ParameterizedTest
    @ValueSource(strings = {"Helsinki", "Kouvola", "tanpere"})
    @NullSource
    public void testRemoveFromFavorites(String location) {
        wAPI.addToFavorites(location);
        wAPI.removeFromFavorites(location);
        assertFalse(wAPI.getLocationFavorites().contains(location),
                "removeFromFavorites should remove '" + location + "' from the favorites list.");

        //removing non-existent location
        wAPI.removeFromFavorites(location);
        assertEquals(0, wAPI.getLocationFavorites().size(),
                "removeFromFavorites should handle removing a non-existent location");
    }

        @ParameterizedTest
        @ValueSource(strings = {"Helsinki", "Kouvola", "tanpere"})
        public void testAddToHistory(String location) {
            wAPI.setLocationActive(location);
            wAPI.setLocationActive(location);
            wAPI.setLocationActive(location); // Duplicate attempt
            assertEquals(1, wAPI.getLocationHistory().size(),
                    "addToHistory should not increase the list size when adding duplicate '" + location + "'");
            assertEquals(location, wAPI.getLocationHistory().get(0),
                    "addToHistory should move '" + location + "' to the front of the history list ");
        }

/* temporarily commented out
    @Test
    public void testSetLocationActiveWithNull() {
        wAPI.setLocationActive(null);
        assertFalse(wAPI.getLocationHistory().contains(null),
                "setLocationActive should not add null to the history list.");
    }

 */
}







