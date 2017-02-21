package pl.com.chodera.myweather.details.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import pl.com.chodera.myweather.BaseFragment;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.common.listeners.WeatherDownloadListener;
import pl.com.chodera.myweather.details.view.WeatherLineChart;
import pl.com.chodera.myweather.model.db.FavoriteLocation;
import pl.com.chodera.myweather.network.DownloadingUtil;
import pl.com.chodera.myweather.network.HandleWeatherResponse;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDetailsFragment extends BaseFragment implements WeatherDownloadListener {

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
    private String weatherInfo;
    private boolean isLocationFavorite = false;
    private FavoriteLocation favoriteLocation;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationName = getArguments().getString(Commons.ArgumentParams.LOCATION_NAME);
            weatherInfo = getArguments().getString(Commons.ArgumentParams.WEATHER_INFO);
        }
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

    private void setupView() {
        setupToolbar();

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

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        changeToBackNavigationMode();
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
    }

    private void setActivityTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }
}
