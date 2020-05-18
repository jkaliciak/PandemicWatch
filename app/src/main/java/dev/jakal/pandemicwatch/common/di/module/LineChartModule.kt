package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import dev.jakal.pandemicwatch.presentation.linechart.LineChartFragment
import dev.jakal.pandemicwatch.presentation.linechart.LineChartViewModel
import dev.jakal.pandemicwatch.presentation.linechart.LineDataConfig
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.ScopeID
import org.koin.dsl.module

/**
 * Module for LineChartFragment
 */
val lineChartModule = module {
    // TODO cleanup module
    scope<LineChartFragment> {
    }
    viewModel { (scopeId: ScopeID, lineChartConfig: LineChartConfig, lineDataConfigs: Array<LineDataConfig>) ->
        LineChartViewModel(
            lineChartConfig,
            lineDataConfigs
        )
    }
}