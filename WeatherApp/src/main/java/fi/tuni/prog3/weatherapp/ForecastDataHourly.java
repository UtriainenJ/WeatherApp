package fi.tuni.prog3.weatherapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;

/**
 * Used for storing and getting data from hourly forecasts (4 days)
 * https://openweathermap.org/api/hourly-forecast
 * @author jerri
 */
public class ForecastDataHourly {
    private static final String METRIC = "metric";    
    private static String units;
    
    private String cod;
    private int cnt;
    private List<WeatherEntry> list;

    /**
     * Set units ("imperial" or "metric")
     * @param str unit
     */
    public void setUnits(String str) {
        ForecastDataHourly.units = str;
    }

    /**
     * Get http call code
     * @return String http code
     */
    public String getCod() {
        return cod;
    }
    
    /**
     * Get number of timestamps
     * @return int num of timestamps
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * Get list of hourly weather info
     * @return
     */
    public List<WeatherEntry> getList() {
        return list;
    }

    /**
     * Class for weather entry
     */
    public static class WeatherEntry {
        private Long dt;
        private MainData main;
        private List<Weather> weather;
        private Clouds clouds;
        private Wind wind;
        private Integer visibility;
        private Double pop;
        private Rain rain;
        private Snow snow;
        private Sys sys;
        private String dt_txt;
        private City city;
        private String country;
        private Integer population;
        private Integer timezone;
        private Long sunrise;
        private Long sunset;

        /**
         * Get time of day in unix, UTC
         * @return
         */
        public String getDt() {
            return dt == null? "null" : String.valueOf(dt);
        }

        /**
         * Get main data object of weather
         * @return MainData object
         */
        public MainData getMain() {
            return main;
        }

        /**
         * Get list of weather descriptors
         * @return List of descriptors
         */
        public List<Weather> getWeather() {
            return weather;
        }

        /**
         * Get Clouds object
         * @return Clouds object
         */
        public Clouds getClouds() {
            return clouds == null? new Clouds() : clouds;
        }

        /**
         * Get Wind object
         * @return Wind object
         */
        public Wind getWind() {
            return wind == null? new Wind() : wind;
        }

        /**
         * Get visibility in meters (0-10000)
         * @return
         */
        public String getVisibility() {
            return visibility == null? "0": String.valueOf(visibility);
        }

        /**
         * Get probability of precipitation
         * @return String risk of rain percentage
         */
        public String getPop() {
            return pop == null? "null" : String.format(Locale.US, "%.0f",100*pop);
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
         * Get Sys object containing part of the day info
         * @return
         */
        public Sys getSys() {
            return sys;
        }

        /**
         * Get formatted time of day (ISO), UTC
         * @return
         */
        public String getDt_txt() {
            return dt_txt;
        }
        
        /**
         * Get local hours
         * @return String hour number
         */
        public String getHour() {
            return dt_txt.substring(11, 13);
        }

        /**
         * Get City object
         * @return City object
         */
        public City getCity() {
            return city == null? new City() : city;
        }

        /**
         * Get country code
         * @return String country code
         */
        public String getCountry() {
            return country == null? "null" : country;
        }

        /**
         * Get population of location
         * @return String population
         */
        public String getPopulation() {
            return population == null? "null" : String.valueOf(population);
        }

        /**
         * Get timezone shift in seconds from UTC, unix time
         * @return String timezone shift
         */
        public String getTimezone() {
            return timezone == null? "null" : String.valueOf(timezone);
        }

        /**
         * Get sunrise time, unix, UTC
         * @return String sunrise time
         */
        public String getSunrise() {
            return sunrise == null? "null" : String.valueOf(sunrise);
        }

        /**
         * Get sunset time, unix, UTC
         * @return String sunset time
         */
        public String getSunset() {
            return sunset == null? "null" : String.valueOf(sunset);
        }
    }

    /**
     * Class containing temperature data
     */
    public static class MainData {
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
         * Get feels like temperature
         * @return String feels like
         */
        public String getFeels_like() {
            return feels_like == null? "null" : String.format(Locale.US, "%.0f", feels_like);
        }

        /**
         * Get minimum temperature of location
         * @return String min temp
         */
        public String getTemp_min() {
            return temp_min == null? "null" : String.format(Locale.US, "%.0f", temp_min);
        }

        /**
         * Get maximum temperature of location
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
         * Get humidity (0-100)
         * @return String humidity
         */
        public String getHumidity() {
            return humidity == null? "0" : String.valueOf(humidity);
        }

        /**
         * Get sea level
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
     * Class containing weather data
     */
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        /**
         * Get weather ID
         * @return int weather ID
         */
        public int getId() {
            return id;
        }

        /**
         * Get main descriptor of weather
         * @return String descriptor
         */
        public String getMain() {
            return main == null? "null" : main;
        }

        /**
         * Get precise descriptor of weather
         * @return String descriptor
         */
        public String getDescription() {
            return description == null? "null" : description;
        }

        /**
         * Get weather icon code
         * @return String icon code
         */
        public String getIcon() {
            return icon == null? "null" : icon;
        }
    }

    /**
     * Class containing cloud data
     */
    public static class Clouds {
        private Integer all;

        /**
         * Get cloud coverage (0-100)
         * @return
         */
        public String getAll() {
            return all == null? "0" : String.valueOf(all);
        }
    }

    /**
     * Class for Wind data
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
         * Get wind direction deviation from 0 degrees
         * @return wind direction
         */
        public String getDeg() {
            return deg == null? "0" : String.valueOf(deg);
        }

        /**
         * Get wind gust
         * @return String gust
         */
        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }
    }

    /**
     * Class for containing rain data
     */
    public static class Rain {
        @SerializedName("1h")
        private Double _1h;

        /**
         * Get rain amount in one hour, mm or inches
         * @return double rain amount
         */
        public double get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? 0.0 : _1h;
            }
            return _1h == null? 0.0 : (_1h/25.4);
        }
    }
    
    /**
     * Class for containing snow data
     */
    public static class Snow {
        @SerializedName("1h")
        private Double _1h;

        /**
         * Get snow amount in one hour, mm or inches
         * @return String snow amount
         */
        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.1f",_1h/25.4);
        }
    }

    /**
     * Class for containing part of day
     */
    public static class Sys {
        private String pod;

        /**
         * Get part of day, "n" or "d" (=night or day)
         * @return String
         */
        public String getPod() {
            return pod;
        }
    }

    /**
     * Class for containing information on cities
     */
    public static class City {
        private Integer id;
        private String name;
        private Coord coord;

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
            return name == null? "null" : name;
        }

        /**
         * Get city coordinates
         * @return Coordinates
         */
        public Coord getCoord() {
            return coord;
        }
    }

    /**
     * Class for containing coordinates
     */
    public static class Coord {
        private double lat;
        private double lon;

        /**
         * Get latitude
         * @return double latitude
         */
        public double getLat() {
            return lat;
        }

        /**
         * Get longitude
         * @return double longitude
         */
        public double getLon() {
            return lon;
        }
    }    
}
