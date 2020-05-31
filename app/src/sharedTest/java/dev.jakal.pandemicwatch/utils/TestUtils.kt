package dev.jakal.pandemicwatch.utils

import dev.jakal.pandemicwatch.common.utils.toLong
import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.model.Timeline
import dev.jakal.pandemicwatch.infrastructure.database.model.*
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoryEntity
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import retrofit2.Response

class TestUtils {

    companion object {

        fun createCountryEntity(countryName: String) = CountryEntity(
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

        fun createCountryHistoryEntity(countryName: String) = CountryHistoryEntity(
            country = countryName
        )

        fun createTimelineEntity(id: Long, countryName: String, type: TimelineType) =
            TimelineEntity(
                id = id,
                country = countryName,
                date = LocalDate.now(),
                amount = 123,
                type = type
            )

        fun createGlobalStatsEntity() = GlobalStatsEntity(
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

        fun createGlobalHistoryEntity() = GlobalHistoryEntity(
            cases = emptyMap(),
            deaths = emptyMap(),
            recovered = emptyMap()
        )

        fun createGlobalStatsNetwork() = GlobalStatsNetwork(
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7.0,
            8.0,
            9,
            10,
            11,
            12.0
        )

        fun createGlobalHistoryNetwork() = createTimelineNetwork()

        fun createCountryNetwork(countryName: String) = CountryNetwork(
            country = countryName,
            countryInfo = CountryInfoNetwork(
                null, null, null, 0.0, 0.0, ""
            ),
            cases = 100,
            todayCases = 10,
            deaths = 10,
            todayDeaths = 1,
            recovered = 50,
            active = 50,
            critical = 10,
            casesPerOneMillion = 1.0,
            deathsPerOneMillion = 2.0,
            updated = LocalDateTime.now().toLong(),
            tests = 30,
            testsPerOneMillion = 0.3,
            continent = "continent"
        )

        fun createCountryHistoricalNetwork(countryName: String) = CountryHistoricalNetwork(
            country = countryName,
            timeline = createTimelineNetwork()
        )

        fun createTimelineNetwork() = TimelineNetwork(
            cases = emptyMap(),
            deaths = emptyMap(),
            recovered = emptyMap()
        )

        fun createCountryHistory(countryName: String, timeline: Timeline) = CountryHistory(
            country = countryName,
            timeline = timeline
        )

        fun createHttpException(errorCode: Int) =
            HttpException(
                Response.error<String>(
                    errorCode,
                    "{}".toResponseBody("application/json".toMediaType())
                )
            )
    }
}