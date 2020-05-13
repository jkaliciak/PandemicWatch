package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.domain.usecase.countryDetails.*
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryDetailsFragment
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryDetailsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.ScopeID
import org.koin.dsl.module

/**
 * Module for CountryDetailsFragment
 */
val countryDetailsModule = module {
    scope<CountryDetailsFragment> {
        scoped {
            LoadCountryUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            UpdateCountryUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            LoadCountryHistoricalUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            UpdateCountryHistoricalUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            AddCountryToFavoritesUseCase(
                get(),
                Dispatchers.Default
            )
        }
        scoped {
            RemoveCountryFromFavoritesUseCase(
                get(),
                Dispatchers.Default
            )
        }
    }
    viewModel { (scopeId: ScopeID, countryName: String) ->
        CountryDetailsViewModel(
            countryName,
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get(),
            getScope(scopeId).get()
        )
    }
}