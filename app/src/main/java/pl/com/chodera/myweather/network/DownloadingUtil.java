package pl.com.chodera.myweather.network;

import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public class DownloadingUtil {

    public static void getCurrentWeather(String location, Callback<WeatherResponse> callback) {
        getRetrofitService().getWeather(location, Commons.OPEN_WEATHER_APP_ID).enqueue(callback);
    }

    public static void getForecastWeather(String location, Callback<WeatherForecastResponse> callback) {
        getRetrofitService().getForecastWeather(location, Commons.OPEN_WEATHER_APP_ID).enqueue(callback);
    }

    private static RestClientService getRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RestClientService.class);
    }
}
