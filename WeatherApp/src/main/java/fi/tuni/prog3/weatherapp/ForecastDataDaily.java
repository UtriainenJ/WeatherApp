package fi.tuni.prog3.weatherapp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

/**
 *
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
    
    public void setUnits(String str) {
        ForecastDataDaily.units = str;
    }

    public City getCity() {
        return city == null? new City() : city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<WeatherEntry> getList() {
        return list;
    }

    public void setList(List<WeatherEntry> list) {
        this.list = list;
    }
    
    public static class City {
        private Integer id;
        private String name;
        private Coord coord;
        private String country;
        private Integer population;
        private Integer timezone;

        public String getId() {
            return id == null? "null" : String.valueOf(id);
        }

        public String getName() {
            return name == null? "null" : name;
        }

        public Coord getCoord() {
            return coord;
        }
        public String getCountry() {
            return country;
        }

        public String getPopulation() {
            return population == null? "0" : String.valueOf(population);
        }

        public Integer getTimezone() {
            return timezone;
        }
    }

    public static class Coord {
        private Double lon;
        private Double lat;

        public Double getLon() {
            return lon;
        }

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

        public String getDt() {
            return dt == null? "null" : String.valueOf(dt);
        }

        public Long getSunrise() {
            return sunrise;
        }

        public Long getSunset() {
            return sunset;
        }

        public Temperature getTemp() {
            return temp == null? new Temperature() : temp;
        }

        public Temperature getFeels_like() {
            return feels_like == null? new Temperature() : feels_like;
        }

        public Integer getPressure() {
            return pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public String getSpeed() {
            return speed == null? "0" : String.format("%.0f", speed);
        }

        public Integer getDeg() {
            return deg;
        }

        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }

        public String getClouds() {
            return clouds == null? "0" : String.valueOf(clouds);
        }

        public String getPop() {
            return pop == null? "0" : String.format(Locale.US, "%.0f",100*pop);
        }

        public String getRain() {
            if(METRIC.equals(units)) {
                return rain == null? "0" : String.format("%.1f", rain);
            }
            return rain == null? "0" : String.format(Locale.US, "%.1f",rain/25.4);
        }

        public String getSnow() {
            if(METRIC.equals(units)) {
                return snow == null? "0" : String.format("%.1f", snow);
            }
            return snow == null? "0" : String.format(Locale.US, "%.1f",snow/25.4);
        }

        public String getWeekday() {
            LocalDateTime ldt = epochToDateTime(dt);
            String day = ldt.getDayOfWeek().toString();
            String dayShort = day.substring(0, 3).toLowerCase();
            dayShort = Character.toUpperCase(dayShort.charAt(0)) + dayShort.substring(1);
            return dayShort;
        }

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

    public static class Temperature {
        private Double day;
        private Double min;
        private Double max;
        private Double night;
        private Double eve;
        private Double morn;

        public String getDay() {
            return day == null? "null" : String.format("%.0f", day);
        }

        public String getMin() {
            return min == null? "null" : String.format("%.0f", min);
        }

        public String getMax() {
            return max == null? "null" : String.format("%.0f", max);
        }
        public String getNight() {
            return night == null? "null" : String.format("%.0f", night);
        }
        public String getEve() {
            return eve == null? "null" : String.format("%.0f", eve);
        }
        public String getMorn() {
            return morn == null? "null" : String.format("%.0f", morn);
        }
    }

    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;

        public Integer getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
        public String getIcon() {
            return icon;
        }
    }
}
