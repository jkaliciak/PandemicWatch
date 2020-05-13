package dev.jakal.pandemicwatch.presentation.overview

data class GlobalStatsPresentation(
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
    val affectedCountries: String,
    val tests: String,
    val testsPerOneMillion: String
)