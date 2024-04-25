package fi.tuni.prog3.weatherapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;

/**
 * Used for storing and getting data for current weather
 * https://openweathermap.org/current
 * @author jerri
 */
public class WeatherData {
    private static final String METRIC = "metric";
    private static String units;
    private String airQuality;

    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Integer visibility;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Clouds clouds;
    private Long dt;
    private Sys sys;
    private Integer timezone;
    private Integer id;
    private String name;
    private int cod;

    /**
     * Set air quality, preferably from class AirQuality.
     * @param aq
     */
    public void setAirQuality(String aq) {
        this.airQuality = aq;
    }
    
    /**
     * Get air quality (Good, Fair, Moderate, Poor, Very Poor)
     * @return String air quality descriptor
     */
    public String getAirQuality() {
        return airQuality;
    }
    
    /**
     * Set units ("metric" or "imperial")
     * @param str unit name
     */
    public void setUnits(String str) {
        WeatherData.units = str;
    }

    /**
     * get Coordinate object
     * @return Coordinate object
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * Get list of different timestamp weather lists
     * @return list of Weather
     */
    public List<Weather> getWeather() {
        return weather;
    }

    /**
     * Get weather data object
     * @return Main object containing weather data
     */
    public Main getMain() {
        return main;
    }

    /**
     *
     * @return String visibility in meters (0-10,000)
     */
    public String getVisibility() {
        return visibility == null? "0" : String.valueOf(visibility);
    }

    /**
     * Get Wind object
     * @return Wind object
     */
    public Wind getWind() {
        return wind == null? new Wind() : wind;
    }

    /**
     * Get Rain object
     * @return Rain object
     */
    public Rain getRain() {
        return rain == null? new Rain() : rain;
    }

    /**
     * Get Snow object
     * @return Snow object
     */
    public Snow getSnow() {
        return snow == null? new Snow() : snow;
    }

    /**
     * Get Clouds object
     * @return Clouds object
     */
    public Clouds getClouds() {
        return clouds == null? new Clouds() : clouds;
    }

    /**
     * Get time of data calculation, unix, UTC
     * @return String unix time UTC of call
     */
    public String getDt() {
        return dt == null? "null" : String.valueOf(dt);
    }

    /**
     * Get Sys object
     * @return Sys object
     */
    public Sys getSys() {
        return sys;
    }

    /**
     * Get timezone
     * @return String shift in seconds from UTC
     */
    public String getTimezone() {
        return timezone == null? "null" : String.valueOf(timezone);
    }

    /**
     * Get city ID
     * @return String city ID
     */
    public String getId() {
        return id == null? "null" : String.valueOf(id);
    }

    /**
     * Get city name
     * @return String city name
     */
    public String getName() {
        return name;
    }

    /**
     * get http call code
     * @return int code
     */
    public int getCod() {
        return cod;
    }
    
    /**
     * Class containing coordinates
     */
    public static class Coord {
        private double lon;
        private double lat;

        /**
         * Get longitude of location
         * @return double longitude
         */
        public double getLon() {
            return lon;
        }

        /**
         * Get latitude of location
         * @return double latitude
         */
        public double getLat() {
            return lat;
        }
    }

    /**
     * Class containing weather descriptors
     */
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        /**
         * Get weather ID
         * https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
         * @return int weather id
         */
        public int getId() {
            return id;
        }

        /**
         * Get weather description (Rain, Snow, Clouds...)
         * @return String weather description
         */
        public String getMain() {
            return main == null? "null" : main;
        }

        /**
         * Get more precise weather description (intensity)
         * @return String weather description
         */
        public String getDescription() {
            return description == null? "No description" : description;
        }

        /**
         * Get icon ID
         * @return String icon ID
         */
        public String getIcon() {
            return icon == null? "null" : icon;
        }
    }

    /**
     * Class containing weather info
     */
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer humidity;
        private Integer sea_level;
        private Integer grnd_level;

        /**
         * Get current temperature
         * @return String temperature
         */
        public String getTemp() {
            return temp == null? "null" : String.format(Locale.US, "%.0f", temp);
        }

        /**
         * Get current feels like temperature
         * @return String feels like
         */
        public String getFeels_like() {
            return feels_like == null? "null" : String.format(Locale.US, "%.0f", feels_like);
        }

        /**
         * Get minimum temperature in an area
         * @return String min temp
         */
        public String getTemp_min() {
            return temp_min == null? "null" : String.format(Locale.US, "%.0f", temp_min);
        }

        /**
         * Get max temperature in an area
         * @return String max temp
         */
        public String getTemp_max() {
            return temp_max == null? "null" : String.format(Locale.US, "%.0f", temp_max);
        }

        /**
         * Get air pressure
         * @return String air pressure
         */
        public String getPressure() {
            return pressure == null? "null" : String.valueOf(pressure);
        }

        /**
         * Get humidity
         * @return String humidity
         */
        public String getHumidity() {
            return humidity == null? "0" : String.valueOf(humidity);
        }

        /**
         * Get sea level of location
         * @return String sea level
         */
        public String getSea_level() {
            return sea_level == null? "null" : String.valueOf(sea_level);
        }

        /**
         * Get ground level
         * @return String ground level
         */
        public String getGrnd_level() {
            return grnd_level == null? "null" : String.valueOf(grnd_level);
        }
    }

    /**
     * Class containing wind data
     */
    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;

        /**
         * Get wind speed (m/s or mph)
         * @return String wind speed
         */
        public String getSpeed() {
            return speed == null? "0" : String.format(Locale.US, "%.0f", speed);
        }

        /**
         * Get direction of wind
         * @return String degrees from 0
         */
        public String getDeg() {
            return deg == null? "0" : String.valueOf(deg);
        }

        /**
         * Get gust of wind (m/s or mph)
         * @return
         */
        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }
    }

    /**
     * Class containing rain data
     */
    public static class Rain {
        @SerializedName("1h")
        private Double _1h;
        @SerializedName("3h")
        private Double _3h;

        /**
         * Get rain data for one hour
         * @return double rain amount 1h
         */
        public double get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? 0.0 : _1h;
            } else {
                return _1h == null? 0.0 : (_1h/25.4);
            }
        }

        /**
         * Get rain data for three hours
         * @return double rain amount 3h
         */
        public double get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? 0.0 : _3h;
            } else {
                return _3h == null? 0.0 : (_3h/25.4);
            }
        }
    }

    /**
     * Class containing snow data
     */
    public static class Snow {
        @SerializedName("1h")
        private Double _1h;
        @SerializedName("3h")
        private Double _3h;

        /**
         * Get snow data for one hour
         * @return String snow amount 1h
         */
        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        /**
         * Get snow data for three hours
         * @return String snow amount 3h
         */
        public String get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? "0" : String.format(Locale.US, "%.1f", _3h);
            }
            return _3h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }
    }

    /**
     * Class containing cloud coverage data
     */
    public static class Clouds {
        private Integer all;

        /**
         * Get cloudiness percentage
         * @return String cloudiness percentage
         */
        public String getAll() {
            return all == null? "0" : String.valueOf(all);
        }
    }

    /**
     * Class containing country data
     */
    public static class Sys {
        private String country;
        private Long sunrise;
        private Long sunset;

        /**
         * Get country code
         * @return String country code
         */
        public String getCountry() {
            return country == null? "null" : country;
        }

        /**
         * Get sunrise time, unix UTC
         * @return
         */
        public String getSunrise() {
            return sunrise == null? "null" : String.valueOf(sunrise);
        }

        /**
         * Get sunset time, unix UTC
         * @return
         */
        public String getSunset() {
            return sunset == null? "null" : String.valueOf(sunset);
        }
    }
}
