package pl.com.chodera.myweather.network.response;

import com.google.gson.Gson;

import org.junit.Test;

import pl.com.chodera.myweather.model.pojo.Main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeatherFormatterUtilTest {

    private final Gson gson = new Gson();

    @Test
    public void getBaseWeatherInfo_formatsTemperatureHumidityAndPressure() {
        WeatherResponse response = gson.fromJson(
                "{\"name\":\"Warsaw\",\"main\":{\"temp\":\"20.5\",\"humidity\":\"55\",\"pressure\":\"1013\"}}",
                WeatherResponse.class);

        String weatherInfo = WeatherFormatterUtil.getBaseWeatherInfo(response);

        assertTrue(weatherInfo.contains("Temperature: 20.5"));
        assertTrue(weatherInfo.contains("°C"));
        assertTrue(weatherInfo.contains("Humidity: 55"));
        assertTrue(weatherInfo.contains("%"));
        assertTrue(weatherInfo.contains("Pressure: 1013"));
        assertTrue(weatherInfo.contains("hPa"));
    }

    @Test
    public void getBaseWeatherInfo_returnsEmptyStringWhenMainMissing() {
        WeatherResponse response = gson.fromJson("{\"name\":\"Warsaw\"}", WeatherResponse.class);

        assertEquals("", WeatherFormatterUtil.getBaseWeatherInfo(response));
    }
}
