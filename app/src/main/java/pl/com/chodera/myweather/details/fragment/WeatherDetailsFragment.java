package pl.com.chodera.myweather.details.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.common.ui.BaseFragment;
import pl.com.chodera.myweather.details.view.WeatherLineChart;
import pl.com.chodera.myweather.model.db.DatabaseHelper;
import pl.com.chodera.myweather.model.db.FavoriteLocation;
import pl.com.chodera.myweather.network.DownloadingUtil;
import pl.com.chodera.myweather.network.listener.WeatherDownloadListener;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.network.response.WeatherResponseCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDetailsFragment extends BaseFragment implements WeatherDownloadListener {

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

    private boolean isLocationFavorite = false;
    private FavoriteLocation favoriteLocation;
    private String locationName;

    public WeatherDetailsFragment() {
        // Required empty public constructor
    }

    public static WeatherDetailsFragment newInstance(String param1, String param2) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Commons.ArgumentParams.LOCATION_NAME, param1);
        args.putString(Commons.ArgumentParams.WEATHER_INFO, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        ButterKnife.bind(this, view);

        setupView();

        getForecastData();

        return view;
    }

    @Override
    public void onWeatherDownloadFailed() {
        currentWeatherInfo.setText(getString(R.string.activity_details_weather_not_found));
    }

    @Override
    public void onWeatherDownloaded(String weatherInfo, String locationName) {
        currentWeatherInfo.setText(weatherInfo);
        this.locationName = locationName;

        setActivityTitle(locationName);
        checkIsLocationSavedAsFavourite();
        setupFavButtonAction();
    }

    private void setupView() {
        setupToolbar(toolbar);
        changeToBackNavigationMode();
        setupCurrentWeatherInfo();

        checkIsLocationSavedAsFavourite();
    }

    private void setupCurrentWeatherInfo() {
        currentWeatherLabel.setText(R.string.activity_details_current_weather_label);

        String weatherInfo = null;
        if (getArguments() != null) {
            locationName = getArguments().getString(Commons.ArgumentParams.LOCATION_NAME);
            weatherInfo = getArguments().getString(Commons.ArgumentParams.WEATHER_INFO);
        }

        if (TextUtils.isEmpty(weatherInfo)) {
            setActivityTitle(getString(R.string.loading_message));
            DownloadingUtil.getCurrentWeather(locationName, new WeatherResponseCallback(this));
        } else {
            setupFavButtonAction();
            currentWeatherInfo.setText(weatherInfo);
            setActivityTitle(locationName);
        }
    }

    private void checkIsLocationSavedAsFavourite() {
        final RealmResults<FavoriteLocation> favoriteLocationRealmResult = getRealmInstance().where(FavoriteLocation.class)
                .equalTo("name", locationName)
                .findAll();

        if (favoriteLocationRealmResult.size() == 1) {
            isLocationFavorite = true;
            favoriteLocation = favoriteLocationRealmResult.first();
            setFavButtonIcon();
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
        if (isLocationFavorite) {
            DatabaseHelper.deleteObjectFromRealm(getRealmInstance(), favoriteLocation);
        } else {
            favoriteLocation = DatabaseHelper.createAndSaveObject(getRealmInstance(), FavoriteLocation.class, locationName);
        }
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

    private void getForecastData() {
        DownloadingUtil.getForecastWeather(locationName, new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                chart.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    chart.setForecastDataToChart(response);
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                chart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setActivityTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }
}
