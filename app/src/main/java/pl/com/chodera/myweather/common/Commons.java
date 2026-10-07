package pl.com.chodera.myweather.common;

import pl.com.chodera.myweather.BuildConfig;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public interface Commons {

    String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    String OPEN_WEATHER_APP_ID = BuildConfig.OPEN_WEATHER_APP_ID;

    int CHART_NUMBER_OF_X_VALUES = 8;

    int FORECAST_FOR_NEXT_NUMBER_OF_HOURS = 24;

    String PASCAL_UNIT = "hPa";
    String CELSIUS_UNIT = "°C";

    interface Chars {
        char SPACE = ' ';
        char COLON = ':';
        String NEW_LINE = System.getProperty("line.separator");
        char H = 'h';
        char PERCENT = '%';
        char ZERO = '0';
    }

    interface IntentKeys {

        String LOCATION_NAME = "key_ADVERTISEMENT";
        String WEATHER_INFO = "key_WEATHER_INFO";
    }

    interface ArgumentParams {

        String LOCATION_NAME = "arg_ADVERTISEMENT";
        String WEATHER_INFO = "arg_WEATHER_INFO";
    }

    interface Animations {

        int DRAW_CHART_DATA = 1800;
    }
}
