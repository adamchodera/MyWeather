package pl.com.chodera.myweather.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import retrofit2.Response;

import static pl.com.chodera.myweather.common.Commons.CELSIUS_UNIT;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class WeatherLineChart extends LineChart {

    public WeatherLineChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setDragEnabled(false);
        setScaleEnabled(false);

        setDrawGridBackground(false);
        setDescription("");
        setNoDataTextDescription(context.getString(R.string.chart_data_not_available));

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final TemperatureValueFormatter temperatureValueFormatter = new TemperatureValueFormatter();
        getAxisLeft().setValueFormatter(temperatureValueFormatter);
        getAxisRight().setEnabled(false);

        Legend l = getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
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

    private List<String> getXValues() {
        ArrayList<String> xValues = new ArrayList<>();

        int hourShift;

        for (int i = 1; i < Commons.CHART_NUMBER_OF_X_VALUES + 1; i++) {
            hourShift = i * 3;
            xValues.add(getHourFormatted(hourShift));
        }
        return xValues;
    }

    private String getHourFormatted(final int hourShift) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, hourShift);

        final SimpleDateFormat df = new SimpleDateFormat("HH", Locale.UK);

        return df.format(c.getTime()) + Commons.Chars.H;
    }

    private ArrayList<ILineDataSet> getYDataSets(final Response<WeatherForecastResponse> response) {
        final ArrayList<Entry> yValues = new ArrayList<>();

        if (response.body().getWeatherForecastList() == null
                || Commons.CHART_NUMBER_OF_X_VALUES > response.body().getWeatherForecastList().size()) {
            return null;
        }
        String tmpTemp;
        for (int i = 0; i < Commons.CHART_NUMBER_OF_X_VALUES; i++) {
            tmpTemp = (response.body().getWeatherForecastList().get(i).getMain().getTemp());
            yValues.add(new Entry(Float.parseFloat(tmpTemp), i));
        }

        final LineDataSet set1 = new LineDataSet(yValues, getContext().getString(R.string.chart_data_legend));

        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setValueFormatter(new TemperatureValueFormatter());
        final ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        return dataSets;
    }

    private class TemperatureValueFormatter implements ValueFormatter, YAxisValueFormatter {

        @Override
        public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
            return getValueFormatted(value);
        }

        @Override
        public String getFormattedValue(final float value, final YAxis yAxis) {
            return getValueFormatted(value);
        }

        private String getValueFormatted(final float value) {
            return new DecimalFormat("#.##").format(value) + CELSIUS_UNIT;
        }
    }
}
