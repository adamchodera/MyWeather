package pl.com.chodera.myweather.network;

import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public interface RestClientService {

    @GET("weather?units=metric")
    Call<WeatherResponse> getWeather(
            @Query("q") String location,
            @Query("appid") String appId);

    @GET("forecast/city?units=metric")
    Call<WeatherForecastResponse> getForecastWeather(
            @Query("q") String location,
            @Query("appid") String appId);
}