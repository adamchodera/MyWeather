package pl.com.chodera.myweather.common;

import pl.com.chodera.myweather.models.pojo.Main;
import pl.com.chodera.myweather.network.response.WeatherResponse;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public class WeatherFormatterUtil {

    public static String getBaseWeatherInfo(WeatherResponse response) {
        try {
            Main main = response.getMain();

            StringBuilder weatherInfo = new StringBuilder();
            weatherInfo.append(getFormattedPair("Temperature", main.getTemp())).append((char) 0x00B0).append(Commons.Chars.CELSIUS_UNIT);
            weatherInfo.append(getFormattedPair("Humidity", main.getHumidity())).append(Commons.Chars.PERCENT);
            weatherInfo.append(getFormattedPair("Pressure", main.getPressure())).append(Commons.PASCAL_UNIT);
            return weatherInfo.toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    private static String getFormattedPair(String label, String value) {
        return Commons.Chars.NEW_LINE + label + Commons.Chars.COLON + Commons.Chars.SPACE + value + Commons.Chars.SPACE;
    }
}
