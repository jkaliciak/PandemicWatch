package dev.jakal.pandemicwatch.presentation.linechart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LineChartViewModel(
    chartConfig: LineChartConfig,
    lineDataConfigs: Array<LineDataConfig>
) : ViewModel() {

    val lineChart: LiveData<LineChartPresentation>
        get() = _lineChart
    private val _lineChart = MutableLiveData(LineChartPresentation(chartConfig, lineDataConfigs))
}