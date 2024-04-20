/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    private static int FIN_OFFSET = 10800;
    
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

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name == null? "null" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPopulation() {
            return population == null? "0" : String.valueOf(population);
        }

        public void setPopulation(Integer population) {
            this.population = population;
        }

        public Integer getTimezone() {
            return timezone;
        }

        public void setTimezone(Integer timezone) {
            this.timezone = timezone;
        }
    }

    public static class Coord {
        private Double lon;
        private Double lat;

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
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

        public void setDt(Long dt) {
            this.dt = dt;
        }

        public Long getSunrise() {
            return sunrise;
        }

        public void setSunrise(Long sunrise) {
            this.sunrise = sunrise;
        }

        public Long getSunset() {
            return sunset;
        }

        public void setSunset(Long sunset) {
            this.sunset = sunset;
        }

        public Temperature getTemp() {
            return temp == null? new Temperature() : temp;
        }

        public void setTemp(Temperature temp) {
            this.temp = temp;
        }

        public Temperature getFeels_like() {
            return feels_like == null? new Temperature() : feels_like;
        }

        public void setFeels_like(Temperature feels_like) {
            this.feels_like = feels_like;
        }

        public Integer getPressure() {
            return pressure;
        }

        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public String getSpeed() {
            return speed == null? "0" : String.format("%.0f", speed);
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Integer getDeg() {
            return deg;
        }

        public void setDeg(Integer deg) {
            this.deg = deg;
        }

        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }

        public void setGust(Double gust) {
            this.gust = gust;
        }

        public String getClouds() {
            return clouds == null? "0" : String.valueOf(clouds);
        }

        public void setClouds(Integer clouds) {
            this.clouds = clouds;
        }

        public String getPop() {
            return pop == null? "0" : String.format(Locale.US, "%.0f",100*pop);
        }

        public void setPop(Double pop) {
            this.pop = pop;
        }

        public String getRain() {
            if(METRIC.equals(units)) {
                return rain == null? "0" : String.format("%.1f", rain);
            }
            return rain == null? "0" : String.format(Locale.US, "%.2f",rain/25.4);
        }

        public void setRain(Double rain) {
            this.rain = rain;
        }
        
        public String getSnow() {
            if(METRIC.equals(units)) {
                return snow == null? "0" : String.format("%.1f", snow);
            }
            return snow == null? "0" : String.format(Locale.US, "%.2f",snow/25.4);
        }

        public void setSnow(Double snow) {
            this.snow = snow;
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
            ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(FIN_OFFSET);
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

        public void setDay(Double day) {
            this.day = day;
        }

        public String getMin() {
            return min == null? "null" : String.format("%.0f", min);
        }

        public void setMin(Double min) {
            this.min = min;
        }

        public String getMax() {
            return max == null? "null" : String.format("%.0f", max);
        }

        public void setMax(Double max) {
            this.max = max;
        }

        public String getNight() {
            return night == null? "null" : String.format("%.0f", night);
        }

        public void setNight(Double night) {
            this.night = night;
        }

        public String getEve() {
            return eve == null? "null" : String.format("%.0f", eve);
        }

        public void setEve(Double eve) {
            this.eve = eve;
        }

        public String getMorn() {
            return morn == null? "null" : String.format("%.0f", morn);
        }

        public void setMorn(Double morn) {
            this.morn = morn;
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

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
