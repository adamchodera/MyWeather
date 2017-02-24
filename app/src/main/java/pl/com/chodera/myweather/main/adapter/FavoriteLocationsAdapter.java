package pl.com.chodera.myweather.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmResults;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.ui.BaseActivity;
import pl.com.chodera.myweather.details.WeatherDetailsActivity;
import pl.com.chodera.myweather.model.db.FavoriteLocation;
import pl.com.chodera.myweather.network.DownloadingUtil;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationViewHolder> {

    private final Context context;

    private final RealmResults<FavoriteLocation> favoriteLocations;

    public FavoriteLocationsAdapter(BaseActivity baseActivity) {
        this.favoriteLocations = baseActivity.getRealmInstance().where(FavoriteLocation.class).findAll();
        this.context = baseActivity;
    }

    @Override
    public FavoriteLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_current_weather, parent, false);

        return new FavoriteLocationViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(FavoriteLocationViewHolder viewHolder, int position) {
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
}
