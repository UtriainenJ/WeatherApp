package fi.tuni.prog3.weatherapp;

import java.util.List;

/**
 * @author Jerri Tarpio
 * Contains information on air quality
 */
public class AirQuality {
    private List<AirQualityList> list;

    /**
     * @return list of air quality
     */
    public List<AirQualityList> getList() {
        return list;
    }
    
    public static class AirQualityList {
        private AirQualityMain main;

        /**
         * @return main
         */
        public AirQualityMain getMain() {
            return main;
        }
    }

    public class AirQualityMain {
        private int aqi;

        /**
         * Air quality index
         * @return int air quality index (1-5)
         */
        public int getAqi() {
            return aqi;
        }
    }
}
