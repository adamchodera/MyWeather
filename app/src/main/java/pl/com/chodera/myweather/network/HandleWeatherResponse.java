package pl.com.chodera.myweather.network;

import pl.com.chodera.myweather.common.WeatherFormatterUtil;
import pl.com.chodera.myweather.common.listeners.WeatherDownloadListener;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class HandleWeatherResponse implements Callback<WeatherResponse> {

    private final WeatherDownloadListener weatherDownloadListener;

    public HandleWeatherResponse(WeatherDownloadListener weatherDownloadListener) {
        this.weatherDownloadListener = weatherDownloadListener;
    }

    @Override
    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
        WeatherResponse weatherResponse = response.body();
        if (weatherResponse == null || weatherResponse.getMain() == null || weatherResponse.getName() == null) {
            weatherDownloadListener.downloadingWeatherFailed();
            return;
        }

        String weatherInfo = WeatherFormatterUtil.getBaseWeatherInfo(weatherResponse);
        if (weatherInfo.length() > 0) {
            weatherDownloadListener.downloadingWeatherSuccessed(weatherInfo, weatherResponse.getName());
        } else {
            weatherDownloadListener.downloadingWeatherFailed();
        }
    }

    @Override
    public void onFailure(Call<WeatherResponse> call, Throwable t) {
        weatherDownloadListener.downloadingWeatherFailed();
    }
}
