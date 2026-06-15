package pl.com.chodera.myweather.network.response;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import okhttp3.ResponseBody;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeatherResponseCallbackTest {

    private final Gson gson = new Gson();
    private final RecordingWeatherDownloadListener listener = new RecordingWeatherDownloadListener();
    private final WeatherResponseCallback callback = new WeatherResponseCallback(listener);
    private final Call<WeatherResponse> call = null;

    @Before
    public void setUp() {
        listener.reset();
    }

    @Test
    public void onResponse_withSuccessfulBody_notifiesListener() {
        WeatherResponse weatherResponse = gson.fromJson(
                "{\"name\":\"Warsaw\",\"main\":{\"temp\":\"20\",\"humidity\":\"55\",\"pressure\":\"1013\"}}",
                WeatherResponse.class);

        callback.onResponse(call, Response.success(weatherResponse));

        assertTrue(listener.downloaded);
        assertEquals("Warsaw", listener.locationName);
        assertTrue(listener.weatherInfo.contains("Temperature"));
    }

    @Test
    public void onResponse_withHttpError_notifiesFailure() {
        callback.onResponse(call, Response.error(404, ResponseBody.create(null, new byte[0])));

        assertTrue(listener.failed);
        assertFalse(listener.downloaded);
    }

    @Test
    public void onFailure_notifiesFailure() {
        callback.onFailure(call, new RuntimeException("network"));

        assertTrue(listener.failed);
    }

    private static class RecordingWeatherDownloadListener implements WeatherDownloadListener {

        boolean downloaded;
        boolean failed;
        String weatherInfo;
        String locationName;

        void reset() {
            downloaded = false;
            failed = false;
            weatherInfo = null;
            locationName = null;
        }

        @Override
        public void onWeatherDownloaded(String weatherInfo, String locationName) {
            downloaded = true;
            this.weatherInfo = weatherInfo;
            this.locationName = locationName;
        }

        @Override
        public void onWeatherDownloadFailed() {
            failed = true;
        }
    }
}
