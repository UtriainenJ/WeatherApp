package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherAPITest {

    @Test
    public void testWeatherAPIConstructor() throws Exception{
        WeatherAPI wAPI = new WeatherAPI();
        assertNull(wAPI.getLocationActive(), "locationActive should be null");

        assertNotNull(wAPI.getLocationFavorites(), "locationFavorites should be an instance of List");
        assertTrue(wAPI.getLocationFavorites().isEmpty(), "locationFavorites should be empty");

        assertNotNull(wAPI.getLocationHistory(), "locationHistory should be an instance of List");
        assertTrue(wAPI.getLocationHistory().isEmpty(), "locationHistory should be empty");
    }


}




