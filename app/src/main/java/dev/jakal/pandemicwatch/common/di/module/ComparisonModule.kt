package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadAvailableComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.AddCountryToComparisonUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.RemoveCountryFromComparisonUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.ResetComparisonCountriesUseCase
import dev.jakal.pandemicwatch.presentation.comparison.ComparisonViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Module for CreateComparisonFragment, SelectComparisonCountryFragment, ComparisonFragment
 */
val comparisonModule = module {
    single {
        LoadComparisonCountriesUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        AddCountryToComparisonUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        RemoveCountryFromComparisonUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        ResetComparisonCountriesUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        LoadAvailableComparisonCountriesUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        LoadComparisonCountriesHistoryUseCase(
            get(),
            Dispatchers.Default
        )
    }
    viewModel {
        ComparisonViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}