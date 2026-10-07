package pl.com.chodera.myweather.details.view;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ForecastTemperatureParserTest {

    private final Gson gson = new Gson();

    @Test
    public void parseTemperatures_returnsValuesForCompleteForecast() {
        ArrayList<Float> temperatures = ForecastTemperatureParser.parseTemperatures(
                forecastWithTemps("1", "2", "3.5", "4", "5", "6", "7", "8"),
                Commons.CHART_NUMBER_OF_X_VALUES);

        assertEquals(Arrays.asList(1f, 2f, 3.5f, 4f, 5f, 6f, 7f, 8f), temperatures);
    }

    @Test
    public void parseTemperatures_returnsNullWhenForecastItemIsNull() {
        List<WeatherResponse> forecasts = new ArrayList<>(forecastWithTemps("1", "2", "3", "4", "5", "6", "7", "8"));
        forecasts.set(3, null);

        assertNull(ForecastTemperatureParser.parseTemperatures(forecasts, Commons.CHART_NUMBER_OF_X_VALUES));
    }

    @Test
    public void parseTemperatures_returnsNullWhenTemperatureIsNotNumeric() {
        assertNull(ForecastTemperatureParser.parseTemperatures(
                forecastWithTemps("1", "2", "hot", "4", "5", "6", "7", "8"),
                Commons.CHART_NUMBER_OF_X_VALUES));
    }

    @Test
    public void parseTemperatures_returnsNullWhenMainIsMissing() {
        List<WeatherResponse> forecasts = new ArrayList<>(forecastWithTemps("1", "2", "3", "4", "5", "6", "7", "8"));
        forecasts.set(1, gson.fromJson("{\"name\":\"Warsaw\"}", WeatherResponse.class));

        assertNull(ForecastTemperatureParser.parseTemperatures(forecasts, Commons.CHART_NUMBER_OF_X_VALUES));
    }

    @Test
    public void parseTemperatures_returnsNullWhenForecastIsTooShort() {
        assertNull(ForecastTemperatureParser.parseTemperatures(
                Collections.singletonList(forecast("10")),
                Commons.CHART_NUMBER_OF_X_VALUES));
    }

    private List<WeatherResponse> forecastWithTemps(String... temps) {
        List<WeatherResponse> forecasts = new ArrayList<>();
        for (String temp : temps) {
            forecasts.add(forecast(temp));
        }
        return forecasts;
    }

    private WeatherResponse forecast(String temp) {
        return gson.fromJson("{\"main\":{\"temp\":\"" + temp + "\"}}", WeatherResponse.class);
    }
}
