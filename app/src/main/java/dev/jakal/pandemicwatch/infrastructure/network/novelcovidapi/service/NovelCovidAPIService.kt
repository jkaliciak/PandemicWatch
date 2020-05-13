package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service

import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.CountryHistoricalNetwork
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.CountryNetwork
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.GlobalStatsNetwork
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.TimelineNetwork
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * https://github.com/NovelCOVID/API
 */
interface NovelCovidAPIService {

    @GET("v2/all")
    suspend fun getGlobalStats(): GlobalStatsNetwork

    @GET("v2/historical/all?lastdays=all")
    suspend fun getGlobalHistorical(): TimelineNetwork

    @GET("v2/countries?sort=country")
    suspend fun getCountries(): List<CountryNetwork>

    @GET("v2/countries/{country}")
    suspend fun getCountry(@Path("country") countryName: String): CountryNetwork

    @GET("v2/historical?lastdays=all")
    suspend fun getHistorical(): List<CountryHistoricalNetwork>

    @GET("v2/historical/{country}?lastdays=all")
    suspend fun getHistorical(@Path("country") countryName: String): CountryHistoricalNetwork
}