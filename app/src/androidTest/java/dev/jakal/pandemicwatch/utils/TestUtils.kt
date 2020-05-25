package dev.jakal.pandemicwatch.utils

import dev.jakal.pandemicwatch.infrastructure.database.model.CountryEntity
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryInfoEmbedded
import org.threeten.bp.LocalDateTime

class TestUtils {

    companion object {

        fun createCountry(countryName: String) = CountryEntity(
            country = countryName,
            countryInfo = CountryInfoEmbedded(),
            cases = 100,
            todayCases = 10,
            deaths = 10,
            todayDeaths = 1,
            recovered = 50,
            active = 50,
            critical = 10,
            casesPerOneMillion = 1.0,
            deathsPerOneMillion = 2.0,
            updated = LocalDateTime.now(),
            tests = 30,
            testsPerOneMillion = 0.3,
            continent = "continent"
        )
    }
}