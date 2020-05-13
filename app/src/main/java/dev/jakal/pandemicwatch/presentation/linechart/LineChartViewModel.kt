package dev.jakal.pandemicwatch.presentation.linechart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LineChartViewModel(
    private val data: LineChartConfig
) : ViewModel() {

    val lineChartConfig: LiveData<LineChartConfig>
        get() = _lineChartData
    private val _lineChartData = MutableLiveData(data)
}