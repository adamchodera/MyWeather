package pl.com.chodera.myweather.network.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import pl.com.chodera.myweather.model.pojo.Clouds;

public class WeatherForecastResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("name")
    private String name;

    @SerializedName("list")
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
