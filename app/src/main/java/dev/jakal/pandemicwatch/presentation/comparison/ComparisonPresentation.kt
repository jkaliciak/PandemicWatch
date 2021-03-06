package dev.jakal.pandemicwatch.presentation.comparison

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.model.CountryHistory

data class ComparisonPresentation(
    val comparisonCountries: List<Country> = emptyList(),
    val comparisonCountriesHistory: List<CountryHistory> = emptyList(),
    val availableCountries: List<Country> = emptyList()
)