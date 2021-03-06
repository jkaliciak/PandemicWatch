package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.presentation.comparison.addcountrytocomparison.AddCountryToComparisonFragment
import dev.jakal.pandemicwatch.presentation.comparison.comparison.ComparisonFragment
import dev.jakal.pandemicwatch.presentation.comparison.createcomparison.CreateComparisonFragment
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryDetailsFragment
import dev.jakal.pandemicwatch.presentation.countrylist.CountryListFragment
import dev.jakal.pandemicwatch.presentation.linechart.LineChartFragment
import dev.jakal.pandemicwatch.presentation.navigation.NavigationActivity
import dev.jakal.pandemicwatch.presentation.overview.OverviewFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

/**
 * Module for NavigationActivity
 */
val navigationModule = module {
    scope<NavigationActivity> {
        fragment { OverviewFragment() }
        fragment { CountryListFragment() }
        fragment { CountryDetailsFragment() }
        fragment { CreateComparisonFragment() }
        fragment { AddCountryToComparisonFragment() }
        fragment { ComparisonFragment() }
        fragment { LineChartFragment() }
    }
}