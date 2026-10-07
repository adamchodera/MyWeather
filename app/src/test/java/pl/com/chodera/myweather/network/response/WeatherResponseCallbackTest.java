package pl.com.chodera.myweather.network.response;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import retrofit2.Call;
import retrofit2.Callback;
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

    @Test
    public void onResponse_withCanceledCall_ignoresSuccessfulBody() {
        callback.onResponse(new FakeCall(true), Response.success(gson.fromJson(
                "{\"name\":\"Krakow\",\"main\":{\"temp\":\"1\",\"humidity\":\"2\",\"pressure\":\"3\"}}",
                WeatherResponse.class)));

        assertFalse(listener.downloaded);
        assertFalse(listener.failed);
        assertEquals(null, listener.locationName);
    }

    @Test
    public void onResponse_withCanceledCall_ignoresHttpError() {
        callback.onResponse(new FakeCall(true), Response.error(500, ResponseBody.create(null, new byte[0])));

        assertFalse(listener.failed);
        assertFalse(listener.downloaded);
    }

    @Test
    public void onFailure_withCanceledCall_ignoresFailure() {
        callback.onFailure(new FakeCall(true), new RuntimeException("canceled"));

        assertFalse(listener.failed);
        assertFalse(listener.downloaded);
    }

    @Test
    public void onResponse_withActiveCall_notifiesListener() {
        callback.onResponse(new FakeCall(false), Response.success(gson.fromJson(
                "{\"name\":\"Gdansk\",\"main\":{\"temp\":\"8\",\"humidity\":\"40\",\"pressure\":\"1000\"}}",
                WeatherResponse.class)));

        assertTrue(listener.downloaded);
        assertFalse(listener.failed);
        assertEquals("Gdansk", listener.locationName);
    }

    private static final class FakeCall implements Call<WeatherResponse> {

        private final boolean canceled;

        private FakeCall(boolean canceled) {
            this.canceled = canceled;
        }

        @Override
        public Response<WeatherResponse> execute() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void enqueue(Callback<WeatherResponse> callback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public Call<WeatherResponse> clone() {
            return new FakeCall(canceled);
        }

        @Override
        public Request request() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }
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
