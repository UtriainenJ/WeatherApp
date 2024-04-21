package fi.tuni.prog3.weatherapp;

import java.util.List;

/**
 *
 * @author jerri
 * ChatGPT used
 */
public class AirQuality {
    private List<AirQualityList> list;

    public List<AirQualityList> getList() {
        return list;
    }
    
    public static class AirQualityList {
        private AirQualityMain main;

        public AirQualityMain getMain() {
            return main;
        }
    }

    public class AirQualityMain {
        private int aqi;

        public int getAqi() {
            return aqi;
        }
    }
}
