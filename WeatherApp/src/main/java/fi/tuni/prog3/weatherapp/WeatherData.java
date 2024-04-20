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
    
    public void setUnits(String str) {
        WeatherData.units = str;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getVisibility() {
        return visibility == null? "0" : String.valueOf(visibility);
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind == null? new Wind() : wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain == null? new Rain() : rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow == null? new Snow() : snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public Clouds getClouds() {
        return clouds == null? new Clouds() : clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getDt() {
        return dt == null? "null" : String.valueOf(dt);
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getTimezone() {
        return timezone == null? "null" : String.valueOf(timezone);
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public String getId() {
        return id == null? "null" : String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
    
    public static class Coord {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
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

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main == null? "null" : main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description == null? "No description" : description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon == null? "null" : icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public String getFeels_like() {
            return feels_like == null? "null" : String.format(Locale.US, "%.0f", feels_like);
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public String getTemp_min() {
            return temp_min == null? "null" : String.format(Locale.US, "%.0f", temp_min);
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public String getTemp_max() {
            return temp_max == null? "null" : String.format(Locale.US, "%.0f", temp_max);
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public String getPressure() {
            return pressure == null? "null" : String.valueOf(pressure);
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public String getHumidity() {
            return humidity == null? "0" : String.valueOf(humidity);
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public String getSea_level() {
            return sea_level == null? "null" : String.valueOf(sea_level);
        }

        public void setSea_level(int sea_level) {
            this.sea_level = sea_level;
        }

        public String getGrnd_level() {
            return grnd_level == null? "null" : String.valueOf(grnd_level);
        }

        public void setGrnd_level(int grnd_level) {
            this.grnd_level = grnd_level;
        }
    }

    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;

        public String getSpeed() {
            return speed == null? "0" : String.format(Locale.US, "%.0f", speed);
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public String getDeg() {
            return deg == null? "0" : String.valueOf(deg);
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }

        public String getGust() {
            return gust == null? "0" : String.format(Locale.US, "%.0f", gust);
        }

        public void setGust(double gust) {
            this.gust = gust;
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

        public void set1h(double _1h) {
            this._1h = _1h;
        }

        public String get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? "0" : String.format(Locale.US, "%.1f", _3h);
            }
            return _3h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public void set3h(double _3h) {
            this._3h = _3h;
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

        public void set1h(double _1h) {
            this._1h = _1h;
        }

        public String get3h() {
            if(METRIC.equals(units)) {
                return _3h == null? "0" : String.format(Locale.US, "%.1f", _3h);
            }
            return _3h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public void set3h(double _3h) {
            this._3h = _3h;
        }
    }

    public static class Clouds {
        private Integer all;

        public String getAll() {
            return all == null? "0" : String.valueOf(all);
        }

        public void setAll(int all) {
            this.all = all;
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

        public void setId(int id) {
            this.id = id;
        }

        public String getCountry() {
            return country == null? "null" : country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getSunrise() {
            return sunrise == null? "null" : String.valueOf(sunrise);
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset == null? "null" : String.valueOf(sunset);
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }
}
