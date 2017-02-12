package pl.com.chodera.myweather.common;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public interface Commons {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    String OPEN_WEATHER_APP_ID = "6fc006e6b8cbdd1bc3fa490b467a1692";

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
    }

    interface Animations {

        int DRAW_CHART_DATA = 1800;
    }
}
