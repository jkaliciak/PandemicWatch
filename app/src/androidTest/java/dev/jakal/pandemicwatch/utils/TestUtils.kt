package dev.jakal.pandemicwatch.utils

import dev.jakal.pandemicwatch.infrastructure.database.model.*
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoryEntity
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity
import org.threeten.bp.LocalDate
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

        fun createCountryHistory(countryName: String) = CountryHistoryEntity(
            country = countryName
        )

        fun createTimeline(id: Long, countryName: String, type: TimelineType) =
            TimelineEntity(
                id = id,
                country = countryName,
                date = LocalDate.now(),
                amount = 123,
                type = type
            )

        fun createGlobalStats() = GlobalStatsEntity(
            cases = 10,
            todayCases = 1,
            deaths = 2,
            todayDeaths = 1,
            recovered = 5,
            active = 3,
            critical = 1,
            casesPerOneMillion = 0.3,
            deathsPerOneMillion = 0.1,
            updated = LocalDateTime.now(),
            affectedCountries = 215,
            tests = 10,
            testsPerOneMillion = 0.2
        )

        fun createGlobalHistory() = GlobalHistoryEntity(
            cases = emptyMap(),
            deaths = emptyMap(),
            recovered = emptyMap()
        )
    }
}