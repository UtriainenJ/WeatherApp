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

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<WeatherEntry> getList() {
        return list;
    }

    public void setList(List<WeatherEntry> list) {
        this.list = list;
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

        public void setDt(long dt) {
            this.dt = dt;
        }

        public MainData getMain() {
            return main;
        }

        public void setMain(MainData main) {
            this.main = main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public Clouds getClouds() {
            return clouds == null? new Clouds() : clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind == null? new Wind() : wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public String getVisibility() {
            return visibility == null? "0": String.valueOf(visibility);
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public String getPop() {
            return pop == null? "null" : String.format(Locale.US, "%.0f",100*pop);
        }

        public void setPop(double pop) {
            this.pop = pop;
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

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDt_txt() {
            return dt_txt;
        }
        
        public String getHour() {
            return dt_txt.substring(11, 13);
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        public City getCity() {
            return city == null? new City() : city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public String getCountry() {
            return country == null? "null" : country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPopulation() {
            return population == null? "null" : String.valueOf(population);
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public String getTimezone() {
            return timezone == null? "null" : String.valueOf(timezone);
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
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
            return temp == null? "null" : String.valueOf(temp);
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public String getFeels_like() {
            return feels_like == null? "null" : String.valueOf(feels_like);
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public String getTemp_min() {
            return temp_min == null? "null" : String.valueOf(temp_min);
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public String getTemp_max() {
            return temp_max == null? "null" : String.valueOf(temp_max);
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

        public double getTemp_kf() {
            return temp_kf;
        }

        public void setTemp_kf(double temp_kf) {
            this.temp_kf = temp_kf;
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
            return description == null? "null" : description;
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

    public static class Clouds {
        private Integer all;

        public String getAll() {
            return all == null? "0" : String.valueOf(all);
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;

        public String getSpeed() {
            return speed == null? "0" : String.valueOf(speed);
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
            return gust == null? "0" : String.valueOf(gust);
        }

        public void setGust(double gust) {
            this.gust = gust;
        }
    }

    public static class Rain {
        @SerializedName("1h")
        private Double _1h;

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public void set1h(double _1h) {
            this._1h = _1h;
        }
    }
    
    public static class Snow {
        @SerializedName("1h")
        private Double _1h;

        public String get1h() {
            if(METRIC.equals(units)) {
                return _1h == null? "0" : String.format(Locale.US, "%.1f", _1h);
            }
            return _1h == null? "0" : String.format(Locale.US, "%.2f",_1h/25.4);
        }

        public void set1h(double _1h) {
            this._1h = _1h;
        }
    }

    public static class Sys {
        private String pod;

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }
    }

    public static class City {
        private Integer id;
        private String name;
        private Coord coord;


        public String getId() {
            return id == null? "null" : String.valueOf(id);
        }

        public void setId(int id) {
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
    }

    public static class Coord {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }    
}
