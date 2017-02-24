package pl.com.chodera.myweather.network.listener;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public interface WeatherDownloadListener {

    void onWeatherDownloaded(final String weatherInfo, final String locationName);

    void onWeatherDownloadFailed();
}
