package dev.jakal.pandemicwatch.presentation.common.chart

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import org.threeten.bp.LocalDate

fun LineChart.setupChart(config: LineChartConfig) {
    setChartData(config)

    // legend
    legend.isEnabled = false

    // description
    description.isEnabled = false

    // touch interactions
    setTouchEnabled(config.touchEnabled)
    isDragEnabled = config.touchEnabled
    isScaleXEnabled = config.touchEnabled
    isScaleYEnabled = config.touchEnabled
    setPinchZoom(config.touchEnabled)
    isDoubleTapToZoomEnabled = config.touchEnabled
    maxHighlightDistance = 300f

    // general
//    setViewPortOffsets(0f, 0f, 0f, 0f)
    setBackgroundColor(ContextCompat.getColor(context, config.backgroundColor))
    setDrawGridBackground(false)

    // x axis
    xAxis.apply {
        valueFormatter =
            DateXAxisFormatter() // value formatter (x axis labels)
        isEnabled = true
        setDrawLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(true)
        position = XAxis.XAxisPosition.BOTTOM
        textColor = ContextCompat.getColor(context, config.onBackgroundColor)
        axisLineColor = ContextCompat.getColor(context, config.onBackgroundColor)
        labelCount = 6
        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
    }

    // y axis
    axisRight.isEnabled = false
    axisLeft.apply {
        isEnabled = true
        setDrawLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(true)
        setDrawZeroLine(true)
        setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        textColor = ContextCompat.getColor(context, config.onBackgroundColor)
        axisLineColor = ContextCompat.getColor(context, config.onBackgroundColor)
        labelCount = 6
        this.axisMinimum = 0f
        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
    }

    // animations
    if (config.animationDuration > 0) {
        animateXY(config.animationDuration, config.animationDuration)
    } else {
        invalidate()
    }
}

fun LineDataSet.setupChartData(chart: LineChart, config: LineChartConfig) {
    // general
    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    cubicIntensity = 0.1f

    // line
    lineWidth = 1.5f
    color = ContextCompat.getColor(chart.context, config.foregroundColor)

    // fill area under line
    setDrawFilled(true)
    fillColor = ContextCompat.getColor(chart.context, config.foregroundColor)
    fillAlpha = 160

    // values
    setDrawValues(true)
    valueTextColor = ContextCompat.getColor(chart.context, config.onForegroundColor)
    valueTextSize = 10f
    valueTypeface = Typeface.create("sans-serif-light", Typeface.NORMAL)

    // circles for values
    setDrawCircles(true)
    circleRadius = 2f
    setCircleColor(ContextCompat.getColor(chart.context, config.foregroundColor))

    // highlights
    setDrawHighlightIndicators(false)
    highLightColor = ContextCompat.getColor(chart.context, config.onBackgroundColor)
}

fun LineChart.setChartData(config: LineChartConfig) {
    val lineDataSet = LineDataSet(config.entries, "")
    lineDataSet.setupChartData(this, config)
    data = LineData(lineDataSet)
}

fun Map<LocalDate, Int>.toEntries() = map { entry ->
    Entry(
        entry.key.toEpochDay().toFloat(),
        entry.value.toFloat()
    )
}