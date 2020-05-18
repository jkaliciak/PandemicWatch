package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.domain.usecase.overview.LoadGlobalHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.overview.LoadGlobalStatsUseCase
import dev.jakal.pandemicwatch.presentation.overview.OverviewFragment
import dev.jakal.pandemicwatch.presentation.overview.OverviewViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.ScopeID
import org.koin.dsl.module

/**
 * Module for OverviewFragment
 */
val overviewModule = module {
    scope<OverviewFragment> {
        scoped {
            LoadGlobalStatsUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            LoadGlobalHistoryUseCase(
                get(),
                Dispatchers.Default
            )
        }
    }
    viewModel { (scopeId: ScopeID) ->
        OverviewViewModel(
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get()
        )
    }
}