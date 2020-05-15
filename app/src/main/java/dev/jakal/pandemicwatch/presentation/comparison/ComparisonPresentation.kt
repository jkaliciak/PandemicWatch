package dev.jakal.pandemicwatch.presentation.comparison

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.model.CountryHistorical

data class ComparisonPresentation(
    val comparisonCountries: List<Country> = emptyList(),
    val comparisonCountriesHistorical: List<CountryHistorical> = emptyList(),
    val availableCountries: List<Country> = emptyList()
)