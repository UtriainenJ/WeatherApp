package fi.tuni.prog3.weatherapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author jerri
 * generated with help of chatGPT
 */
public class ForecastDataHourly {
    private static final String METRIC = "metric";    
    private static String units;
    
    private String cod;
    private int message;
    private int cnt;
    private List<WeatherEntry> list;
    
    public ForecastDataHourly(){}
    
    public void setUnits(String str) {
        ForecastDataHourly.units = str;
    }

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<WeatherEntry> getList() {
        return list;
    }

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

        public String getDt() {
            return dt == null? "null" : String.valueOf(dt);
        }

        public MainData getMain() {
            return main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public Clouds getClouds() {
            return clouds == null? new Clouds() : clouds;
        }

        public Wind getWind() {
            return wind == null? new Wind() : wind;
        }

        public String getVisibility() {
            return visibility == null? "0": String.valueOf(visibility);
        }

        public String getPop() {
            return pop == null? "null" : String.format(Locale.US, "%.0f",100*pop);
        }

        public Rain getRain() {
            return rain == null? new Rain() : rain;
        }
        
        public Snow getSnow() {
            return snow == null? new Snow() : snow;
        }

        public Sys getSys() {
            return sys;
        }

        public String getDt_txt() {
            return dt_txt;
        }
        
        public String getHour() {
            return dt_txt.substring(11, 13);
        }

        public City getCity() {
            return city == null? new City() : city;
        }

        public String getCountry() {
            return country == null? "null" : country;
        }

        public String getPopulation() {
            return population == null? "null" : String.valueOf(population);
        }

        public String getTimezone() {
            return timezone == null? "null" : String.valueOf(timezone);
        }

        public String getSunrise() {
            return sunrise == null? "null" : String.valueOf(sunrise);
        }

        public String getSunset() {
            return sunset == null? "null" : String.valueOf(sunset);
        }
    }

    public static class MainData {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer humidity;
        private Integer sea_level;
        private Integer grnd_level;
        private double temp_kf;

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

        public double getTemp_kf() {
            return temp_kf;
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
            return description == null? "null" : description;
        }

        public String getIcon() {
            return icon == null? "null" : icon;
        }
    }

    public static class Clouds {
        private Integer all;

        public String getAll() {
            return all == null? "0" : String.valueOf(all);
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

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.1f",_1h/25.4);
        }
    }
    
    public static class Snow {
        @SerializedName("1h")
        private Double _1h;

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.1f",_1h/25.4);
        }
    }

    public static class Sys {
        private String pod;

        public String getPod() {
            return pod;
        }
    }

    public static class City {
        private Integer id;
        private String name;
        private Coord coord;


        public String getId() {
            return id == null? "null" : String.valueOf(id);
        }

        public String getName() {
            return name == null? "null" : name;
        }

        public Coord getCoord() {
            return coord;
        }
    }

    public static class Coord {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }    
}
