package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.domain.usecase.countrylist.LoadCountriesUseCase
import dev.jakal.pandemicwatch.presentation.countrylist.CountryListFragment
import dev.jakal.pandemicwatch.presentation.countrylist.CountryListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.ScopeID
import org.koin.dsl.module

/**
 * Module for CountryListFragment
 */
val countryListModule = module {
    scope<CountryListFragment> {
        scoped {
            LoadCountriesUseCase(
                get(),
                Dispatchers.Default
            )
        }
    }
    viewModel { (scopeId: ScopeID) ->
        CountryListViewModel(
            getScope(scopeId).get(),
            getScope(scopeId).get()
        )
    }
}