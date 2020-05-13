package dev.jakal.pandemicwatch.presentation.countrydetails

data class CountryPresentation(
    val country: String,
    val cases: String,
    val todayCases: String,
    val deaths: String,
    val todayDeaths: String,
    val recovered: String,
    val active: String,
    val critical: String,
    val casesPerOneMillion: String,
    val deathsPerOneMillion: String,
    val updated: String,
    val tests: String,
    val testsPerOneMillion: String,
    val continent: String,
    val lat: Double,
    val long: Double,
    val flagUrl: String,
    val favorite: Boolean
)