package pl.com.chodera.myweather.common;

import android.util.Log;

import pl.com.chodera.myweather.BuildConfig;

public class Logger {

    public static void log(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
}
