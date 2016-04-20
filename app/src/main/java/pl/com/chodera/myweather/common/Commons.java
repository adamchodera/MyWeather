package pl.com.chodera.myweather.common;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public class Commons {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static final String OPEN_WEATHER_APP_ID = "6fc006e6b8cbdd1bc3fa490b467a1692";

    public static final int CHART_NUMBER_OF_X_VALUES = 8;

    public static final String PASCAL_UNIT = "hPa";

    public interface Chars {

        char SPACE = ' ';
        char COLON = ':';
        String NEW_LINE = System.getProperty("line.separator");
        char PLUS = '+';
        char H = 'h';
        char CELSIUS_UNIT = 'C';
        char PERCENT = '%';
    }

    public interface IntentKeys {

        String LOCATION_NAME = "key_ADVERTISEMENT";
    }

    public interface Animations {

        int DRAW_CHART_DATA = 1800;
    }
}
