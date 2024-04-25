package fi.tuni.prog3.weatherapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author jerri
 * chatGPT used for generating all this silliness
 */
public class WeatherData {
    
    private static final String METRIC = "metric";
    private static String units;
    private String airQuality;

    private Coord coord;
    private List<Weather> weather;
    private String base;
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

    public WeatherData() {}
    
    public void setAirQuality(String aq) {
        this.airQuality = aq;
    }
    
    public String getAirQuality() {
        return airQuality;
    }
    
    public void setUnits(String str) {
        WeatherData.units = str;
    }

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public String getVisibility() {
        return visibility == null? "0" : String.valueOf(visibility);
    }

    public Wind getWind() {
        return wind == null? new Wind() : wind;
    }

    public Rain getRain() {
        return rain == null? new Rain() : rain;
    }

    public Snow getSnow() {
        return snow == null? new Snow() : snow;
    }

    public Clouds getClouds() {
        return clouds == null? new Clouds() : clouds;
    }

    public String getDt() {
        return dt == null? "null" : String.valueOf(dt);
    }

    public Sys getSys() {
        return sys;
    }

    public String getTimezone() {
        return timezone == null? "null" : String.valueOf(timezone);
    }

    public String getId() {
        return id == null? "null" : String.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }
    
    public static class Coord {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main == null? "null" : main;
        }

        public String getDescription() {
            return description == null? "No description" : description;
        }

        public String getIcon() {
            return icon == null? "null" : icon;
        }
    }

    public static class Main {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer humidity;
        private Integer sea_level;
        private Integer grnd_level;

        public String getTemp() {
            return temp == null? "null" : String.format(Locale.US, "%.0f", temp);
        }

        public String getFeels_like() {
            return feels_like == null? "null" : String.format(Locale.US, "%.0f", feels_like);
        }

        public String getTemp_min() {
            return temp_min == null? "null" : String.format(Locale.US, "%.0f", temp_min);
        }

        public String getTemp_max() {
            return temp_max == null? "null" : String.format(Locale.US, "%.0f", temp_max);
        }

        public String getPressure() {
            return pressure == null? "null" : String.valueOf(pressure);
        }

        public String getHumidity() {
            return humidity == null? "0" : String.valueOf(humidity);
        }

        public String getSea_level() {
            return sea_level == null? "null" : String.valueOf(sea_level);
        }

        public String getGrnd_level() {
            return grnd_level == null? "null" : String.valueOf(grnd_level);
        }
    }

    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;

        public String getSpeed() {
            return speed == null? "0" : String.format(Locale.US, "%.0f", speed);
        }

        public String getDeg() {
            return deg == null? "0" : String.valueOf(deg);
        }

        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }
    }

    public static class Rain {
        @SerializedName("1h")
        private Double _1h;
        @SerializedName("3h")
        private Double _3h;

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public String get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? "0" : String.format(Locale.US, "%.1f", _3h);
            }
            return _3h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }
    }

    public static class Snow {
        @SerializedName("1h")
        private Double _1h;
        @SerializedName("3h")
        private Double _3h;

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public String get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? "0" : String.format(Locale.US, "%.1f", _3h);
            }
            return _3h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }
    }

    public static class Clouds {
        private Integer all;

        public String getAll() {
            return all == null? "0" : String.valueOf(all);
        }
    }

    public static class Sys {
        private Integer id;
        private String country;
        private Long sunrise;
        private Long sunset;

        public String getId() {
            return id == null? "null" : String.valueOf(id);
        }

        public String getCountry() {
            return country == null? "null" : country;
        }

        public String getSunrise() {
            return sunrise == null? "null" : String.valueOf(sunrise);
        }

        public String getSunset() {
            return sunset == null? "null" : String.valueOf(sunset);
        }
    }
}
