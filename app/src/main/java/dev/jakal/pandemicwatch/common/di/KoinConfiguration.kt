package dev.jakal.pandemicwatch.common.di

import dev.jakal.pandemicwatch.common.di.module.*

val allModules = listOf(
    appModule,
    dataModule,
    repositoryModule,
    networkModule,
    navigationModule,
    overviewModule,
    countryListModule,
    countryDetailsModule,
    lineChartModule,
    comparisonModule
)