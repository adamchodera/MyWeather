package pl.com.chodera.myweather.details.view;

import java.util.ArrayList;
import java.util.List;

import pl.com.chodera.myweather.model.pojo.Main;
import pl.com.chodera.myweather.network.response.WeatherResponse;

final class ForecastTemperatureParser {

    private ForecastTemperatureParser() {
    }

    static ArrayList<Float> parseTemperatures(List<WeatherResponse> weatherForecastList, int count) {
        if (weatherForecastList == null || count > weatherForecastList.size()) {
            return null;
        }

        final ArrayList<Float> temperatures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            WeatherResponse forecast = weatherForecastList.get(i);
            if (forecast == null) {
                return null;
            }
            Main main = forecast.getMain();
            if (main == null || main.getTemp() == null) {
                return null;
            }
            try {
                temperatures.add(Float.parseFloat(main.getTemp()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return temperatures;
    }
}
