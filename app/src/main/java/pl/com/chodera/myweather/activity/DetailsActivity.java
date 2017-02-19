package pl.com.chodera.myweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.common.listeners.WeatherDownloadListener;
import pl.com.chodera.myweather.model.db.FavoriteLocation;
import pl.com.chodera.myweather.network.DownloadingUtil;
import pl.com.chodera.myweather.network.HandleWeatherResponse;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.view.WeatherLineChart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends BaseActivity implements WeatherDownloadListener {

    @Bind(R.id.id_activity_details_coordinator_layout)
    public CoordinatorLayout coordinatorLayout;

    @Bind(R.id.toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.item_primary_text)
    public TextView currentWeatherLabel;

    @Bind(R.id.item_current_weather_info)
    public TextView currentWeatherInfo;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    @Bind(R.id.fab)
    public FloatingActionButton floatingActionButton;

    @Bind(R.id.chart)
    public WeatherLineChart chart;

    private String locationName;

    private boolean isLocationFavorite = false;
    private FavoriteLocation favoriteLocation;

    public static void goToDetailsScreen(final Context context, final String locationName, final String weatherInfo) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Commons.IntentKeys.LOCATION_NAME, locationName);
        intent.putExtra(Commons.IntentKeys.WEATHER_INFO, weatherInfo);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        locationName = getIntent().getStringExtra(Commons.IntentKeys.LOCATION_NAME);
        final String weatherInfo = getIntent().getStringExtra(Commons.IntentKeys.WEATHER_INFO);

        setupView(weatherInfo);

        getForecastData();
    }

    private void setupView(String weatherInfo) {
        setSupportActionBar(toolbar);
        changeToBackNavigationMode();

        checkIsLocationSavedAsFavourite();
        setFavButtonIcon();
        
        currentWeatherLabel.setText(R.string.activity_details_current_weather_label);
        if (TextUtils.isEmpty(weatherInfo)) {
            setActivityTitle(getString(R.string.loading_message));
            DownloadingUtil.getWeather(locationName, new HandleWeatherResponse(this));
        } else {
            setupFavButtonAction();
            currentWeatherInfo.setText(weatherInfo);
            setActivityTitle(locationName);
        }
    }

    private void checkIsLocationSavedAsFavourite() {
        RealmResults<FavoriteLocation> favoriteLocationRealmResult = getRealmInstance().where(FavoriteLocation.class)
                .equalTo("name", locationName)
                .findAll();

        if (favoriteLocationRealmResult.size() == 1) {
            isLocationFavorite = true;
            favoriteLocation = favoriteLocationRealmResult.get(0);
        }
    }

    private void setupFavButtonAction() {
        floatingActionButton.setOnClickListener(view -> {
            addOrRemoveLocationFromFavorites();
            isLocationFavorite = !isLocationFavorite;
            setFavButtonIcon();
            showSnackBar();
        });
    }

    private void addOrRemoveLocationFromFavorites() {
        getRealmInstance().beginTransaction();

        if (isLocationFavorite) {
            if (favoriteLocation != null) {
                favoriteLocation.deleteFromRealm();
            }
        } else {
            FavoriteLocation newFavoriteLocation = new FavoriteLocation(locationName);
            getRealmInstance().copyToRealm(newFavoriteLocation);
            favoriteLocation = newFavoriteLocation;
        }

        getRealmInstance().commitTransaction();
    }

    private void setFavButtonIcon() {
        if (isLocationFavorite) {
            floatingActionButton.setImageResource(R.drawable.ic_action_favorited);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_action_not_favorited);
        }
    }

    private void showSnackBar() {
        String message;
        if (isLocationFavorite) {
            message = getString(R.string.activity_details_location_saved_as_favourite);
        } else {
            message = getString(R.string.activity_details_location_removed_from_favourite);
        }

        Snackbar.make(floatingActionButton, message, Snackbar.LENGTH_LONG).show();
    }

    private void setTitleWeatherNotFound() {
        currentWeatherInfo.setText(getString(R.string.activity_details_weather_not_found));
    }

    private void getForecastData() {
        DownloadingUtil.getForecastWeather(locationName, new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                chart.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    drawChart(response);
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                chart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void drawChart(Response<WeatherForecastResponse> response) {
        chart.setForecastDataToChart(response);
        chart.animateX(Commons.Animations.DRAW_CHART_DATA, Easing.EasingOption.EaseInOutQuart);
    }

    private void setActivityTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void downloadingWeatherFailed() {
        setTitleWeatherNotFound();
    }

    @Override
    public void downloadingWeatherSucceeded(String weatherInfo, String name) {
        currentWeatherInfo.setText(weatherInfo);

        locationName = name;
        setActivityTitle(locationName);
        checkIsLocationSavedAsFavourite();
        setupFavButtonAction();
        setFavButtonIcon();
    }

    @Override
    protected View getCoordinatorLayoutView() {
        return coordinatorLayout;
    }

    @Override
    protected void internetIsAvailableAgain() {
        DownloadingUtil.getWeather(locationName, new HandleWeatherResponse(this));
    }
}