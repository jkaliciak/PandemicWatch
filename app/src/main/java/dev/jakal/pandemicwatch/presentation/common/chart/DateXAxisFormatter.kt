package dev.jakal.pandemicwatch.presentation.common.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import dev.jakal.pandemicwatch.common.utils.toLocalDate
import dev.jakal.pandemicwatch.common.utils.toDateFormat

class DateXAxisFormatter : ValueFormatter() {

    // format from epochDays to date/month
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return value.toLong().toLocalDate().toDateFormat()
    }
}