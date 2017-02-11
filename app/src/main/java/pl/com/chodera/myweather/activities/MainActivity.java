package pl.com.chodera.myweather.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.adapters.FavoriteLocationsAdapter;
import pl.com.chodera.myweather.common.listeners.WeatherSearchViewListener;

public class MainActivity extends BaseActivity {

    @Bind(R.id.id_activity_main_coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.id_activity_main_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.id_activity_main_tutorial_text_view)
    TextView tutorialTextView;

    private FavoriteLocationsAdapter favoriteLocationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        changeToWithLogoNavigationMode();

        setupFavoriteLocationsAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new WeatherSearchViewListener(this));

        return true;
    }

    @Override
    protected View getCoordinatorLayoutView() {
        return coordinatorLayout;
    }

    @Override
    protected void internetIsAvailableAgain() {
        fetchAndDisplayWeatherForFavorites();
    }

    private void setupFavoriteLocationsAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchAndDisplayWeatherForFavorites();
    }

    private void fetchAndDisplayWeatherForFavorites() {
        favoriteLocationsAdapter = new FavoriteLocationsAdapter(this);

        if (favoriteLocationsAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            tutorialTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(favoriteLocationsAdapter);
        }
    }
}
