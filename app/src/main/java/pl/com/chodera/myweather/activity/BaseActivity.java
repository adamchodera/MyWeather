package pl.com.chodera.myweather.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObjectSchema;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.NetworkReceiver;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public abstract class BaseActivity extends AppCompatActivity implements NetworkReceiver.NetworkStateChangeListener {

    protected boolean previousInternetAvailability = true;

    private Realm realmInstance;
    private Snackbar snackbar;
    private NetworkReceiver receiver;

    protected abstract View getCoordinatorLayoutView();

    protected abstract void internetIsAvailableAgain();

    public void changeToBackNavigationMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);
    }

    public void changeToWithLogoNavigationMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logo);
    }


    public Realm getRealmInstance() {
        if (realmInstance == null || realmInstance.isClosed()) {

            Realm.init(this);
            final RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
            realmInstance = Realm.getInstance(realmConfiguration);
        }

        return realmInstance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public void onNetworkStateChanged(final boolean isInternetAvailable) {
        if (!previousInternetAvailability && isInternetAvailable) {
            // internet connection is available once again
            internetIsAvailableAgain();
        }
        previousInternetAvailability = isInternetAvailable;

        displayOrHideSnackbarWithInfo(isInternetAvailable);
    }

    private void displayOrHideSnackbarWithInfo(final boolean isInternetAvailable) {
        if (isInternetAvailable && snackbar != null) {
            snackbar.dismiss();
        } else if (!isInternetAvailable) {
            if (snackbar == null) {
                snackbar = Snackbar.make(getCoordinatorLayoutView(), getString(R.string.no_internet_connection),
                        Snackbar.LENGTH_INDEFINITE);
            }
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Registers BroadcastReceiver to track network connection changes.
        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver(this);
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        realmInstance.close();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }
}
