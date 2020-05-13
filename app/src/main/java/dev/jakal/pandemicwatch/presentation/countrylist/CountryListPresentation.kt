package dev.jakal.pandemicwatch.presentation.countrylist

import dev.jakal.pandemicwatch.domain.model.Country

data class CountryListPresentation(
    val countries: List<Country>
)