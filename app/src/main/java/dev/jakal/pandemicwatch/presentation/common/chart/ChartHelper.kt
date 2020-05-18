package dev.jakal.pandemicwatch.presentation.common.chart

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import dev.jakal.pandemicwatch.presentation.linechart.LineDataConfig
import org.threeten.bp.LocalDate

fun LineChart.setupChart(chartConfig: LineChartConfig, vararg lineConfigs: LineDataConfig) {
    setChartData(chartConfig, *lineConfigs)

    val backgroundColor = ContextCompat.getColor(context, chartConfig.backgroundColor)
    val onBackgroundColor = ContextCompat.getColor(context, chartConfig.onBackgroundColor)
    val typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)

    // legend
    legend.apply {
        isEnabled = chartConfig.legendEnabled
        form = Legend.LegendForm.CIRCLE
        isWordWrapEnabled = true
        textColor = onBackgroundColor
        this.typeface = typeface
    }

    // description
    description.isEnabled = false

    // touch interactions
    setTouchEnabled(chartConfig.touchEnabled)
    isDragEnabled = chartConfig.touchEnabled
    isScaleXEnabled = chartConfig.touchEnabled
    isScaleYEnabled = chartConfig.touchEnabled
    setPinchZoom(chartConfig.touchEnabled)
    isDoubleTapToZoomEnabled = chartConfig.touchEnabled
    maxHighlightDistance = 300f

    // general
    setBackgroundColor(backgroundColor)
    setDrawGridBackground(false)

    // x axis
    xAxis.apply {
        valueFormatter = DateXAxisFormatter() // value formatter (x axis labels)
        isEnabled = true
        setDrawLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(true)
        position = XAxis.XAxisPosition.BOTTOM
        textColor = onBackgroundColor
        axisLineColor = onBackgroundColor
        labelCount = 6
        this.typeface = typeface
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
        textColor = onBackgroundColor
        axisLineColor = onBackgroundColor
        labelCount = 6
        axisMinimum = 0f
        this.typeface = typeface
    }

    // animations
    if (chartConfig.animationDuration > 0) {
        animateXY(chartConfig.animationDuration, chartConfig.animationDuration)
    } else {
        invalidate()
    }
}

fun LineDataSet.setupChartData(
    chart: LineChart,
    chartConfig: LineChartConfig,
    lineConfig: LineDataConfig
) {
    val foregroundColor = ContextCompat.getColor(chart.context, lineConfig.foregroundColor)
    val onForegroundColor = ContextCompat.getColor(chart.context, lineConfig.onForegroundColor)

    // general
    mode = LineDataSet.Mode.LINEAR
    cubicIntensity = 0.1f

    // line
    lineWidth = 1.5f
    color = foregroundColor

    // fill area under line
    setDrawFilled(true)
    fillColor = foregroundColor
    fillAlpha = if (chartConfig.fillEnabled) 160 else 0

    // values
    setDrawValues(true)
    valueTextColor = onForegroundColor
    valueTextSize = 10f
    valueTypeface = Typeface.create("sans-serif-light", Typeface.NORMAL)

    // circles for values
    setDrawCircles(true)
    circleRadius = 2f
    setCircleColor(foregroundColor)
    circleHoleColor = foregroundColor

    // highlights
    setDrawHighlightIndicators(false)
}

fun LineChart.setChartData(chartConfig: LineChartConfig, vararg lineConfigs: LineDataConfig) {
    data = LineData(
        lineConfigs.map {
            LineDataSet(it.entries, it.label).apply {
                setupChartData(this@setChartData, chartConfig, it)
            }
        }
    )
}

fun Map<LocalDate, Int>.toEntries() = map { entry ->
    Entry(
        entry.key.toEpochDay().toFloat(),
        entry.value.toFloat()
    )
}

fun List<CountryHistory>.toCaseLineDataConfigs(): Array<LineDataConfig> =
    mapIndexed { index, countryHistory ->
        LineDataConfig(
            entries = countryHistory.timeline.cases.toEntries(),
            label = countryHistory.country,
            foregroundColor = comparisonColors[index % comparisonColors.size],
            onForegroundColor = R.color.colorChartOnForeground
        )
    }.toTypedArray()

fun List<CountryHistory>.toDeathsLineDataConfigs(): Array<LineDataConfig> =
    mapIndexed { index, countryHistory ->
        LineDataConfig(
            entries = countryHistory.timeline.deaths.toEntries(),
            label = countryHistory.country,
            foregroundColor = comparisonColors[index % comparisonColors.size],
            onForegroundColor = R.color.colorChartOnForeground
        )
    }.toTypedArray()

fun List<CountryHistory>.toRecoveredLineDataConfigs(): Array<LineDataConfig> =
    mapIndexed { index, countryHistory ->
        LineDataConfig(
            entries = countryHistory.timeline.recovered.toEntries(),
            label = countryHistory.country,
            foregroundColor = comparisonColors[index % comparisonColors.size],
            onForegroundColor = R.color.colorChartOnForeground
        )
    }.toTypedArray()

private val comparisonColors = listOf(
    R.color.colorChartComparison1,
    R.color.colorChartComparison3,
    R.color.colorChartComparison4,
    R.color.colorChartComparison2
)