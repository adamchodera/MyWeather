package pl.com.chodera.myweather.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.ui.BaseActivity;
import pl.com.chodera.myweather.details.WeatherDetailsActivity;
import pl.com.chodera.myweather.model.db.FavoriteLocation;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationViewHolder> {

    static final String PAYLOAD_FORCE_REFRESH = "force-refresh";

    private final Context context;

    private final RealmResults<FavoriteLocation> favoriteLocations;

    private final OrderedRealmCollectionChangeListener<RealmResults<FavoriteLocation>> favoritesListener =
            (results, changeSet) -> applyFavoritesChange(changeSet);

    private Runnable visibilityListener;

    public FavoriteLocationsAdapter(BaseActivity baseActivity) {
        this.favoriteLocations = baseActivity.getRealmInstance().where(FavoriteLocation.class).findAll();
        this.context = baseActivity;
    }

    public void setVisibilityListener(Runnable visibilityListener) {
        this.visibilityListener = visibilityListener;
    }

    public void startObserving() {
        if (favoriteLocations.isValid()) {
            favoriteLocations.addChangeListener(favoritesListener);
        }
    }

    public void stopObserving() {
        if (favoriteLocations.isValid()) {
            favoriteLocations.removeChangeListener(favoritesListener);
        }
    }

    public void refreshWeather() {
        int count = getItemCount();
        if (count > 0) {
            notifyItemRangeChanged(0, count, PAYLOAD_FORCE_REFRESH);
        }
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
        bindLocation(viewHolder, position, false);
    }

    @Override
    public void onBindViewHolder(FavoriteLocationViewHolder viewHolder, int position, List<Object> payloads) {
        if (payloads != null && payloads.contains(PAYLOAD_FORCE_REFRESH)) {
            bindLocation(viewHolder, position, true);
            return;
        }
        super.onBindViewHolder(viewHolder, position, payloads);
    }

    @Override
    public void onViewRecycled(FavoriteLocationViewHolder holder) {
        holder.cancelActiveRequest();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        try {
            return favoriteLocations.size();
        } catch (IllegalStateException e) {
            // Realm can be closed while the activity is finishing.
            return 0;
        }
    }

    private void bindLocation(FavoriteLocationViewHolder viewHolder, int position, boolean forceRefresh) {
        FavoriteLocation favoriteLocation = favoriteLocations.get(position);
        if (favoriteLocation == null) {
            return;
        }
        String locationName = favoriteLocation.getName();
        if (locationName == null) {
            return;
        }

        viewHolder.bind(locationName, forceRefresh);
        viewHolder.rootView.setOnClickListener(
                v -> WeatherDetailsActivity.goToDetailsScreen(
                        context,
                        locationName,
                        viewHolder.infoAboutWeather.getText().toString()));
    }

    private void applyFavoritesChange(OrderedCollectionChangeSet changeSet) {
        if (changeSet == null) {
            notifyVisibility();
            return;
        }

        OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
        for (int i = deletions.length - 1; i >= 0; i--) {
            notifyItemRangeRemoved(deletions[i].startIndex, deletions[i].length);
        }
        for (OrderedCollectionChangeSet.Range range : changeSet.getInsertionRanges()) {
            notifyItemRangeInserted(range.startIndex, range.length);
        }
        for (OrderedCollectionChangeSet.Range range : changeSet.getChangeRanges()) {
            notifyItemRangeChanged(range.startIndex, range.length);
        }
        notifyVisibility();
    }

    private void notifyVisibility() {
        if (visibilityListener != null) {
            visibilityListener.run();
        }
    }
}
