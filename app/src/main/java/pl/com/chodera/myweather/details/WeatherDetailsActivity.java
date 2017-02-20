package pl.com.chodera.myweather.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.activity.BaseActivity;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.details.fragment.WeatherDetailsFragment;

public class WeatherDetailsActivity extends BaseActivity {

    public static void goToDetailsScreen(final Context context, final String locationName, final String weatherInfo) {
        Intent intent = new Intent(context, WeatherDetailsActivity.class);
        intent.putExtra(Commons.IntentKeys.LOCATION_NAME, locationName);
        intent.putExtra(Commons.IntentKeys.WEATHER_INFO, weatherInfo);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            setupFragment();
        }
    }

    @Override
    protected View getCoordinatorLayoutView() {
        return null;
    }

    @Override
    protected void internetIsAvailableAgain() {

    }

    private void setupFragment() {
        final String locationName = getIntent().getStringExtra(Commons.IntentKeys.LOCATION_NAME);
        final String weatherInfo = getIntent().getStringExtra(Commons.IntentKeys.WEATHER_INFO);
        final WeatherDetailsFragment fragment = WeatherDetailsFragment.newInstance(locationName, weatherInfo);

        getSupportFragmentManager().beginTransaction().add(R.id.id_activity_forecast_weather_root_view, fragment).commit();
    }
}