package pl.com.chodera.myweather.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import pl.com.chodera.myweather.R;
import pl.com.chodera.myweather.common.Commons;
import pl.com.chodera.myweather.network.response.WeatherForecastResponse;
import retrofit2.Response;

/**
 * Created by Adam Chodera on 2016-03-17.
 */
public class WeatherLineChart extends LineChart {

    public WeatherLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDragEnabled(false);
        setScaleEnabled(false);

        setDrawGridBackground(false);
        setDescription("");
        setNoDataTextDescription(context.getString(R.string.chart_data_not_available));

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void setForecastDataToChart(Response<WeatherForecastResponse> response) {
        ArrayList<String> xValues = new ArrayList<>();

        String hourShift;
        for (int i = 1; i < Commons.CHART_NUMBER_OF_X_VALUES + 1; i++) {
            hourShift = String.valueOf(i * 3);
            xValues.add(Commons.Chars.PLUS + hourShift + Commons.Chars.H);
        }

        ArrayList<Entry> yValues = new ArrayList<>();

        try {
            String tmpTemp;
            for (int i = 0; i < Commons.CHART_NUMBER_OF_X_VALUES; i++) {
                tmpTemp = (response.body().getWeatherForecastList().get(i).getMain().getTemp());
                yValues.add(new Entry(Float.parseFloat(tmpTemp), i));
            }

            LineDataSet set1 = new LineDataSet(yValues, getContext().getString(R.string.chart_data_legend));

            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(xValues, dataSets);
            setData(data);

            Legend l = getLegend();
            l.setForm(Legend.LegendForm.CIRCLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
