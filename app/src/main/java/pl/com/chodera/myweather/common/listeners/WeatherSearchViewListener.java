package pl.com.chodera.myweather.common.listeners;

import android.content.Context;
import android.support.v7.widget.SearchView;
import pl.com.chodera.myweather.activities.DetailsActivity;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class WeatherSearchViewListener implements SearchView.OnQueryTextListener {

    private final Context context;

    public WeatherSearchViewListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        DetailsActivity.goToDetailsScreen(context, query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
