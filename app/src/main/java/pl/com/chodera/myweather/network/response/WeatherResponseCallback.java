package pl.com.chodera.myweather.network.response;

import pl.com.chodera.myweather.common.listener.WeatherDownloadListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class WeatherResponseCallback implements Callback<WeatherResponse> {

    private final WeatherDownloadListener weatherDownloadListener;

    public WeatherResponseCallback(final WeatherDownloadListener weatherDownloadListener) {
        this.weatherDownloadListener = weatherDownloadListener;
    }

    @Override
    public void onResponse(final Call<WeatherResponse> call, final Response<WeatherResponse> response) {
        final WeatherResponse weatherResponse = response.body();
        if (weatherResponse == null || weatherResponse.getMain() == null || weatherResponse.getName() == null) {
            weatherDownloadListener.onWeatherDownloadFailed();
            return;
        }

        final String weatherInfo = WeatherFormatterUtil.getBaseWeatherInfo(weatherResponse);
        if (weatherInfo.length() > 0) {
            weatherDownloadListener.onWeatherDownloaded(weatherInfo, weatherResponse.getName());
        } else {
            weatherDownloadListener.onWeatherDownloadFailed();
        }
    }

    @Override
    public void onFailure(final Call<WeatherResponse> call, final Throwable t) {
        weatherDownloadListener.onWeatherDownloadFailed();
    }
}
