package pl.com.chodera.myweather.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import pl.com.chodera.myweather.network.response.WeatherResponseCallback;

class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

    final LinearLayout rootView;
    final TextView locationName;
    final TextView infoAboutWeather;

    FavoriteLocationViewHolder(View itemView) {
        super(itemView);

        locationName = (TextView) itemView.findViewById(R.id.item_primary_text);
        infoAboutWeather = (TextView) itemView.findViewById(R.id.item_current_weather_info);
        rootView = (LinearLayout) itemView.findViewById(R.id.root_view);
    }

    WeatherResponseCallback getCallback() {
        return new WeatherResponseCallback(new WeatherDownloadListener() {
            @Override
            public void onWeatherDownloadFailed() {
                infoAboutWeather.setText(R.string.favorite_location_adapter_downloading_weather_failed);
            }

            @Override
            public void onWeatherDownloaded(String weatherInfo, String locationName) {
                infoAboutWeather.setText(weatherInfo);
            }
        });
    }
}
