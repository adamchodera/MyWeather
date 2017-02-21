package pl.com.chodera.myweather.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkReceiver extends BroadcastReceiver {

    private final NetworkStateChangeListener listener;

    public NetworkReceiver(final NetworkStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();


        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        listener.onNetworkStateChanged(isConnected);
    }

    public interface NetworkStateChangeListener {
        void onNetworkStateChanged(boolean isInternetAvailable);
    }
}