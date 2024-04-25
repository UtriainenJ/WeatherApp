package fi.tuni.prog3.weatherapp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

/**
 * Used for storing and getting data from daily forecasts (max 16 days)
 * @author jerri
 * https://openweathermap.org/forecast16
 */
public class ForecastDataDaily {
    private static final String METRIC = "metric";    
    private static String units;
    
    private City city;
    private String cod;
    private Double message;
    private Integer cnt;
    private List<WeatherEntry> list;
    
    /**
     * Set units ("imperial" or "metric")
     * @param str unit
     */
    public void setUnits(String str) {
        ForecastDataDaily.units = str;
    }

    /**
     * Get city object
     * @return City object
     */
    public City getCity() {
        return city == null? new City() : city;
    }

    /**
     * Get http code
     * @return String http code
     */
    public String getCod() {
        return cod;
    }

    /**
     * Get count of days
     * @return Integer count of days
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     * Get List of days' weather info
     * @return list
     */
    public List<WeatherEntry> getList() {
        return list;
    }
    
    /**
     * Contains info on city
     */
    public static class City {
        private Integer id;
        private String name;
        private Coord coord;
        private String country;
        private Integer population;
        private Integer timezone;

        /**
         * Get name of city
         * @return
         */
        public String getName() {
            return name == null? "null" : name;
        }

        /**
         * Get coordinates object
         * @return Coord object
         */
        public Coord getCoord() {
            return coord;
        }

        /**
         * Get country code
         * @return String country code
         */
        public String getCountry() {
            return country;
        }

        /**
         * Get city population
         * @return String population
         */
        public String getPopulation() {
            return population == null? "0" : String.valueOf(population);
        }

        /**
         * Get timezone, shift in seconds from unix UTC
         * @return
         */
        public Integer getTimezone() {
            return timezone;
        }
    }

    /**
     * Class city containing coordinate data
     */
    public static class Coord {
        private Double lon;
        private Double lat;

        /**
         * Get longitude
         * @return Double longitude
         */
        public Double getLon() {
            return lon;
        }

        /**
         * Get latitude
         * @return Double latitude
         */
        public Double getLat() {
            return lat;
        }
    }

    public static class WeatherEntry {
        private Long dt;
        private Long sunrise;
        private Long sunset;
        private Temperature temp;
        private Temperature feels_like;
        private Integer pressure;
        private Integer humidity;
        private List<Weather> weather;
        private Double speed;
        private Integer deg;
        private Double gust;
        private Integer clouds;
        private Double pop;
        private Double rain;
        private Double snow;

        /**
         * Get time of day of api call, unix UTC
         * @return
         */
        public String getDt() {
            return dt == null? "null" : String.valueOf(dt);
        }

        /**
         * Get time of sunrise, unix UTC
         * @return
         */
        public Long getSunrise() {
            return sunrise;
        }

        /**
         * Get time of sunset, unix UTC
         * @return
         */
        public Long getSunset() {
            return sunset;
        }

        /**
         * Get temperature object
         * @return Temperature object
         */
        public Temperature getTemp() {
            return temp == null? new Temperature() : temp;
        }

        /**
         * Get feels like object
         * @return Temperature feels like object
         */
        public Temperature getFeels_like() {
            return feels_like == null? new Temperature() : feels_like;
        }

        /**
         * Get air pressure
         * @return Integer air pressure
         */
        public Integer getPressure() {
            return pressure;
        }

        /**
         * Get humidity
         * @return Integer humidity
         */
        public Integer getHumidity() {
            return humidity;
        }

        /**
         * Get weather descriptors
         * @return List collection of weather information
         */
        public List<Weather> getWeather() {
            return weather;
        }

        /**
         * Get wind speed
         * @return String wind speed (m/s or mph)
         */
        public String getSpeed() {
            return speed == null? "0" : String.format("%.0f", speed);
        }

        /**
         * Get wind direction deviation from 0 degrees
         * @return Integer degrees
         */
        public Integer getDeg() {
            return deg;
        }

        /**
         * Get wind gust
         * @return String wind gust (m/s or mph)
         */
        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }

        /**
         * Get cloud coverage (0-100%)
         * @return String cloud coverage
         */
        public String getClouds() {
            return clouds == null? "0" : String.valueOf(clouds);
        }

        /**
         * Get probability of precipitation
         * @return String risk of rain
         */
        public String getPop() {
            return pop == null? "0" : String.format(Locale.US, "%.0f",100*pop);
        }

        /**
         * Get rain amount in mm or inches
         * @return String rain amount
         */
        public String getRain() {
            if(METRIC.equals(units)) {
                return rain == null? "0" : String.format("%.1f", rain);
            }
            return rain == null? "0" : String.format(Locale.US, "%.1f",rain/25.4);
        }

        /**
         * Get snow amount in mm or inches
         * @return String snow amount
         */
        public String getSnow() {
            if(METRIC.equals(units)) {
                return snow == null? "0" : String.format("%.1f", snow);
            }
            return snow == null? "0" : String.format(Locale.US, "%.1f",snow/25.4);
        }

        /**
         * Get weekday (Mon, Tue, Wed, ...)
         * @return String weekday
         */
        public String getWeekday() {
            LocalDateTime ldt = epochToDateTime(dt);
            String day = ldt.getDayOfWeek().toString();
            String dayShort = day.substring(0, 3).toLowerCase();
            dayShort = Character.toUpperCase(dayShort.charAt(0)) + dayShort.substring(1);
            return dayShort;
        }

        /**
         * Get data in format 'date'.'month'
         * @return String calendar date
         */
        public String getDate() {
            LocalDateTime ldt = epochToDateTime(dt);
            return ldt.getDayOfMonth() + "." + ldt.getMonthValue();
        }
        
        private static LocalDateTime epochToDateTime(long epochSeconds) {
            Instant instant = Instant.ofEpochSecond(epochSeconds);
            ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(0);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneOffset);
            return localDateTime;
        }
    }

    /**
     * Class containing temperature info on different times of day
     */
    public static class Temperature {
        private Double day;
        private Double min;
        private Double max;
        private Double night;
        private Double eve;
        private Double morn;

        /**
         * Get temperature at 12.00 local time
         * @return String temp
         */
        public String getDay() {
            return day == null? "null" : String.format("%.0f", day);
        }

        /**
         * Get minimum temp during day
         * @return String min temp
         */
        public String getMin() {
            return min == null? "null" : String.format("%.0f", min);
        }

        /**
         * Get maximum temp during day
         * @return String max temp
         */
        public String getMax() {
            return max == null? "null" : String.format("%.0f", max);
        }
        
        /**
         * Get temperature at 00:00 local time
         * @return
         */
        public String getNight() {
            return night == null? "null" : String.format("%.0f", night);
        }
        
        /**
         * Get temperature at 18:00 local time
         * @return
         */
        public String getEve() {
            return eve == null? "null" : String.format("%.0f", eve);
        }
        
        /**
         * Get temperature at 06:00 local time
         * @return
         */
        public String getMorn() {
            return morn == null? "null" : String.format("%.0f", morn);
        }
    }

    /**
     * Class containing weather data
     */
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;

        /**
         * Get weather ID
         * @return Integer weather ID
         */
        public Integer getId() {
            return id;
        }

        /**
         * Get weather descriptor
         * @return String descriptor
         */
        public String getMain() {
            return main;
        }

        /**
         * get more precise weather descriptor
         * @return String description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Get weather icon ID
         * @return String icon ID
         */
        public String getIcon() {
            return icon;
        }
    }
}
