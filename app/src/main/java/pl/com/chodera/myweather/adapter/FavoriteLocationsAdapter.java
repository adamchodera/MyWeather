package pl.com.chodera.myweather.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.BaseActivity;
import pl.com.chodera.myweather.common.listener.WeatherDownloadListener;
import pl.com.chodera.myweather.details.WeatherDetailsActivity;
import pl.com.chodera.myweather.model.db.FavoriteLocation;
import pl.com.chodera.myweather.network.DownloadingUtil;
import pl.com.chodera.myweather.network.response.WeatherResponseCallback;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationsAdapter.ViewHolder> {

    private final Context context;

    private final RealmResults<FavoriteLocation> favoriteLocations;

    public FavoriteLocationsAdapter(BaseActivity baseActivity) {
        this.favoriteLocations = baseActivity.getRealmInstance().where(FavoriteLocation.class).findAll();
        this.context = baseActivity;
    }

    @Override
    public FavoriteLocationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_current_weather, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(FavoriteLocationsAdapter.ViewHolder viewHolder, int position) {
        FavoriteLocation favoriteLocation = favoriteLocations.get(position);

        String locationName = favoriteLocation.getName();
        viewHolder.locationName.setText(locationName);
        DownloadingUtil.getCurrentWeather(locationName, viewHolder.getCallback());
        viewHolder.cardView.setOnClickListener(
                v -> WeatherDetailsActivity.goToDetailsScreen(
                        context,
                        locationName,
                        viewHolder.infoAboutWeather.getText().toString()));
    }

    @Override
    public int getItemCount() {
        try {
            return favoriteLocations.size();
        } catch (IllegalStateException e) {
            // TODO handle Realm.io reinitialization after returning from background
            // quick fix for returning to main activity from the background
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;

        private final TextView locationName;
        private final TextView infoAboutWeather;

        public ViewHolder(View itemView) {
            super(itemView);

            locationName = (TextView) itemView.findViewById(R.id.item_primary_text);
            infoAboutWeather = (TextView) itemView.findViewById(R.id.item_current_weather_info);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }


        public WeatherResponseCallback getCallback() {
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

}
