package pl.com.chodera.myweather.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.network.DownloadingUtil;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import pl.com.chodera.myweather.network.response.WeatherResponseCallback;
import retrofit2.Call;

class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

    final LinearLayout rootView;
    final TextView locationName;
    final TextView infoAboutWeather;

    private String boundLocationName;
    private Call<?> activeWeatherCall;
    private int bindGeneration;

    FavoriteLocationViewHolder(View itemView) {
        super(itemView);

        locationName = (TextView) itemView.findViewById(R.id.item_primary_text);
        infoAboutWeather = (TextView) itemView.findViewById(R.id.item_current_weather_info);
        rootView = (LinearLayout) itemView.findViewById(R.id.root_view);
    }

    void bind(String locationName, boolean forceRefresh) {
        boolean sameLocation = locationName.equals(boundLocationName);
        if (sameLocation && !forceRefresh && hasActiveRequest()) {
            return;
        }

        cancelActiveRequest();
        final int requestId = bindGeneration;
        boundLocationName = locationName;
        this.locationName.setText(locationName);
        if (!sameLocation) {
            infoAboutWeather.setText(R.string.loading_message);
        }

        Call<WeatherResponse> weatherCall = DownloadingUtil.newCurrentWeatherCall(locationName);
        activeWeatherCall = weatherCall;
        weatherCall.enqueue(new WeatherResponseCallback(listenerFor(requestId)));
    }

    void cancelActiveRequest() {
        bindGeneration++;
        if (activeWeatherCall != null && !activeWeatherCall.isCanceled()) {
            activeWeatherCall.cancel();
        }
        activeWeatherCall = null;
    }

    private boolean hasActiveRequest() {
        return activeWeatherCall != null && !activeWeatherCall.isCanceled();
    }

    private boolean isCurrentRequest(int requestId) {
        return requestId == bindGeneration
                && activeWeatherCall != null
                && !activeWeatherCall.isCanceled();
    }

    private WeatherDownloadListener listenerFor(final int requestId) {
        return new WeatherDownloadListener() {
            @Override
            public void onWeatherDownloadFailed() {
                if (!isCurrentRequest(requestId)) {
                    return;
                }
                infoAboutWeather.setText(R.string.favorite_location_adapter_downloading_weather_failed);
            }

            @Override
            public void onWeatherDownloaded(String weatherInfo, String downloadedLocationName) {
                if (!isCurrentRequest(requestId)) {
                    return;
                }
                infoAboutWeather.setText(weatherInfo);
            }
        };
    }
}
