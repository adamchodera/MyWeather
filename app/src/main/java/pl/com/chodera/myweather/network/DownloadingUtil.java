package pl.com.chodera.myweather.network;

import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public class DownloadingUtil {

    private static RestClientService retrofitService;

    public static Call<WeatherResponse> newCurrentWeatherCall(String location) {
        return getRetrofitService().getWeather(location, Commons.OPEN_WEATHER_APP_ID);
    }

    public static Call<WeatherResponse> getCurrentWeather(String location, Callback<WeatherResponse> callback) {
        Call<WeatherResponse> call = newCurrentWeatherCall(location);
        call.enqueue(callback);
        return call;
    }

    public static Call<WeatherForecastResponse> getForecastWeather(String location, Callback<WeatherForecastResponse> callback) {
        Call<WeatherForecastResponse> call = getRetrofitService().getForecastWeather(location, Commons.OPEN_WEATHER_APP_ID);
        call.enqueue(callback);
        return call;
    }

    private static RestClientService getRetrofitService() {
        if (retrofitService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Commons.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitService = retrofit.create(RestClientService.class);
        }
        return retrofitService;
    }
}
