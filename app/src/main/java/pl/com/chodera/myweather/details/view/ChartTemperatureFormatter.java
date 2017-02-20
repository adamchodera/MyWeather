package pl.com.chodera.myweather.details.view;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import pl.com.chodera.myweather.common.Commons;

import static pl.com.chodera.myweather.common.Commons.CELSIUS_UNIT;

class ChartTemperatureFormatter implements ValueFormatter, YAxisValueFormatter {

    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        return getValueFormatted(value);
    }

    @Override
    public String getFormattedValue(final float value, final YAxis yAxis) {
        return getValueFormatted(value);
    }

    private String getValueFormatted(final float value) {
        return new DecimalFormat("#.#").format(value) + CELSIUS_UNIT + Commons.Chars.SPACE + Commons.Chars.SPACE;
    }
}