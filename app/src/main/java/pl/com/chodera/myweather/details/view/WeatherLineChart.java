package pl.com.chodera.myweather.details.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import pl.com.chodera.myweather.network.response.WeatherResponse;
import retrofit2.Response;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class WeatherLineChart extends LineChart {

    public WeatherLineChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setDragEnabled(false);
        setScaleEnabled(false);
        setDrawGridBackground(false);

        setupLabels(context);
        setupAxis();
        setupLegend();
    }

    public void setForecastDataToChart(final Response<WeatherForecastResponse> response) {
        final List<String> xValues = getXValues();
        final ArrayList<ILineDataSet> yDataSets = getYDataSets(response);

        if (xValues == null || yDataSets == null) {
            return;
        }

        final LineData data = new LineData(xValues, yDataSets);
        setData(data);
    }

    private void setupLabels(final Context context) {
        setDescription("");
        setNoDataTextDescription(context.getString(R.string.chart_data_not_available));
    }

    private void setupAxis() {
        final XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final ChartTemperatureFormatter chartTemperatureFormatter = new ChartTemperatureFormatter();
        getAxisRight().setValueFormatter(chartTemperatureFormatter);
        getAxisLeft().setEnabled(false);
    }

    private void setupLegend() {
        final Legend l = getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        l.setForm(Legend.LegendForm.CIRCLE);
    }

    private List<String> getXValues() {
        final ArrayList<String> xValues = new ArrayList<>();

        int hourShift;
        int multiplier = Commons.FORECAST_FOR_NEXT_NUMBER_OF_HOURS / Commons.CHART_NUMBER_OF_X_VALUES;
        for (int i = 1; i < Commons.CHART_NUMBER_OF_X_VALUES + 1; i++) {
            hourShift = i * multiplier;
            xValues.add(getHourFormatted(hourShift));
        }
        return xValues;
    }

    private ArrayList<ILineDataSet> getYDataSets(final Response<WeatherForecastResponse> response) {
        final List<WeatherResponse> weatherForecastList = response.body().getWeatherForecastList();

        if (weatherForecastList == null || Commons.CHART_NUMBER_OF_X_VALUES > weatherForecastList.size()) {
            return null;
        }

        final ArrayList<ILineDataSet> yDataSets = new ArrayList<>();
        yDataSets.add(getForecastTemperatureData(weatherForecastList));
        return yDataSets;
    }

    private LineDataSet getForecastTemperatureData(List<WeatherResponse> weatherForecastList) {
        final ArrayList<Entry> forecastTemperatureDataList = parseForecastTemperatureToEntryList(weatherForecastList);

        final LineDataSet forecastTemperatureData = new LineDataSet(forecastTemperatureDataList, getContext().getString(R.string.chart_data_legend));
        forecastTemperatureData.enableDashedLine(10f, 5f, 0f);
        forecastTemperatureData.enableDashedHighlightLine(10f, 5f, 0f);
        forecastTemperatureData.setColor(Color.BLACK);
        forecastTemperatureData.setCircleColor(Color.BLACK);
        forecastTemperatureData.setLineWidth(1f);
        forecastTemperatureData.setCircleRadius(3f);
        forecastTemperatureData.setDrawCircleHole(false);
        forecastTemperatureData.setValueTextSize(9f);
        forecastTemperatureData.setDrawFilled(true);
        forecastTemperatureData.setValueFormatter(new ChartTemperatureFormatter());

        return forecastTemperatureData;
    }

    @NonNull
    private ArrayList<Entry> parseForecastTemperatureToEntryList(List<WeatherResponse> weatherForecastList) {
        final ArrayList<Entry> forecastTemperatureDataList = new ArrayList<>();
        String tmpTemp;
        for (int i = 0; i < Commons.CHART_NUMBER_OF_X_VALUES; i++) {
            tmpTemp = weatherForecastList.get(i).getMain().getTemp();
            forecastTemperatureDataList.add(new Entry(Float.parseFloat(tmpTemp), i));
        }
        return forecastTemperatureDataList;
    }

    private String getHourFormatted(final int hourShift) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, hourShift);

        final SimpleDateFormat df = new SimpleDateFormat("HH", Locale.UK);
        String hourFormatted = df.format(c.getTime()) + Commons.Chars.H;
        if (hourFormatted.charAt(0) == Commons.Chars.ZERO) {
            // convert eg. 01h into 1h for better readability
            hourFormatted = hourFormatted.substring(1, hourFormatted.length());
        }

        return hourFormatted;
    }
}
