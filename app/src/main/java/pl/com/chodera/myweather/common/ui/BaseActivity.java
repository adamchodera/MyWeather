package pl.com.chodera.myweather.common.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.realm.Realm;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.model.db.DatabaseHelper;
import pl.com.chodera.myweather.network.NetworkReceiver;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public abstract class BaseActivity extends AppCompatActivity implements NetworkReceiver.NetworkStateChangeListener {

    protected boolean previousInternetAvailability = true;

    private Snackbar snackbar;
    private NetworkReceiver receiver;
    private Realm realmInstance;

    @Nullable
    protected abstract View getCoordinatorLayoutView();

    protected abstract void internetIsAvailableAgain();

    public Realm getRealmInstance() {
        if (DatabaseHelper.isRealmInstanceClosed(realmInstance)) {
            realmInstance = Realm.getDefaultInstance();
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

        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (DatabaseHelper.isRealmInstanceOpened(realmInstance)) {
            realmInstance.close();
        }
    }

    protected void changeToWithLogoNavigationMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logo);
    }

    private void displayOrHideSnackbarWithInfo(final boolean isInternetAvailable) {
        if (getCoordinatorLayoutView() == null) {
            // TODO think again if it's needed
            return;
        }
        if (isInternetAvailable && snackbar != null) {
            snackbar.dismiss();
        } else if (!isInternetAvailable) {
            if (snackbar == null) {
                snackbar = Snackbar.make(
                        getCoordinatorLayoutView(),
                        getString(R.string.no_internet_connection),
                        Snackbar.LENGTH_INDEFINITE);
            }
            snackbar.show();
        }
    }
}
