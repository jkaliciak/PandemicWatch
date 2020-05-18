package dev.jakal.pandemicwatch.presentation.linechart

data class LineChartPresentation(
    val chartConfig: LineChartConfig,
    val lineConfigs: Array<LineDataConfig>
)