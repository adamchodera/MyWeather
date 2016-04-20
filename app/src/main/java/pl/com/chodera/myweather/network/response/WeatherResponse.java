package pl.com.chodera.myweather.network.response;

import pl.com.chodera.myweather.models.pojo.Clouds;
import pl.com.chodera.myweather.models.pojo.Coord;
import pl.com.chodera.myweather.models.pojo.Main;

public class WeatherResponse {

    private Clouds clouds;

    private Coord coord;

    private Main main;

    private String name;

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }
}
