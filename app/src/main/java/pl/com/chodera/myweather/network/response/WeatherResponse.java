package pl.com.chodera.myweather.network.response;

import com.google.gson.annotations.SerializedName;

import pl.com.chodera.myweather.models.pojo.Clouds;
import pl.com.chodera.myweather.models.pojo.Coord;
import pl.com.chodera.myweather.models.pojo.Main;

public class WeatherResponse {

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("main")
    private Main main;

    @SerializedName("name")
    private String name;

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }
}
