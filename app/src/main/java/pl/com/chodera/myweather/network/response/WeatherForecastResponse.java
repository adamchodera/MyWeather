package pl.com.chodera.myweather.network.response;

import java.util.List;
import pl.com.chodera.myweather.models.pojo.Clouds;

public class WeatherForecastResponse {

    private Clouds clouds;

    private String id;

    private String name;

    private List<WeatherResponse> list;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<WeatherResponse> getWeatherForecastList() {
        return list;
    }
}
