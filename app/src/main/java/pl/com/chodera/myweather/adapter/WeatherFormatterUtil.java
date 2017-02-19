package pl.com.chodera.myweather.adapter;

import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.model.pojo.Main;
import pl.com.chodera.myweather.network.response.WeatherResponse;

import static pl.com.chodera.myweather.common.Commons.CELSIUS_UNIT;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public class WeatherFormatterUtil {

    public static String getBaseWeatherInfo(final WeatherResponse response) {
        try {
            final Main main = response.getMain();

            final StringBuilder weatherInfo = new StringBuilder();
            weatherInfo.append(getFormattedPair("Temperature", main.getTemp())).append(CELSIUS_UNIT);
            weatherInfo.append(Commons.Chars.NEW_LINE);
            weatherInfo.append(getFormattedPair("Humidity", main.getHumidity())).append(Commons.Chars.PERCENT);
            weatherInfo.append(Commons.Chars.NEW_LINE);
            weatherInfo.append(getFormattedPair("Pressure", main.getPressure())).append(Commons.PASCAL_UNIT);
            return weatherInfo.toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    private static String getFormattedPair(final String label, final String value) {
        return label + Commons.Chars.COLON + Commons.Chars.SPACE + value + Commons.Chars.SPACE;
    }
}
