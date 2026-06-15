package pl.com.chodera.myweather.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import pl.com.chodera.myweather.network.response.WeatherResponseCallback;
import retrofit2.Call;

class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

    final LinearLayout rootView;
    final TextView locationName;
    final TextView infoAboutWeather;

    private String boundLocationName;
    private Call<?> activeWeatherCall;
    private final WeatherResponseCallback weatherCallback;

    FavoriteLocationViewHolder(View itemView) {
        super(itemView);

        locationName = (TextView) itemView.findViewById(R.id.item_primary_text);
        infoAboutWeather = (TextView) itemView.findViewById(R.id.item_current_weather_info);
        rootView = (LinearLayout) itemView.findViewById(R.id.root_view);

        weatherCallback = new WeatherResponseCallback(new WeatherDownloadListener() {
            @Override
            public void onWeatherDownloadFailed() {
                if (!isBoundToActiveRequest()) {
                    return;
                }
                infoAboutWeather.setText(R.string.favorite_location_adapter_downloading_weather_failed);
            }

            @Override
            public void onWeatherDownloaded(String weatherInfo, String downloadedLocationName) {
                if (!isBoundToActiveRequest()) {
                    return;
                }
                infoAboutWeather.setText(weatherInfo);
            }
        });
    }

    void bind(String locationName, Call<?> weatherCall) {
        cancelActiveRequest();

        boundLocationName = locationName;
        activeWeatherCall = weatherCall;
        this.locationName.setText(locationName);
        infoAboutWeather.setText(R.string.loading_message);
    }

    WeatherResponseCallback getWeatherCallback() {
        return weatherCallback;
    }

    void cancelActiveRequest() {
        if (activeWeatherCall != null && !activeWeatherCall.isCanceled()) {
            activeWeatherCall.cancel();
        }
        activeWeatherCall = null;
    }

    private boolean isBoundToActiveRequest() {
        return boundLocationName != null
                && activeWeatherCall != null
                && !activeWeatherCall.isCanceled();
    }
}
