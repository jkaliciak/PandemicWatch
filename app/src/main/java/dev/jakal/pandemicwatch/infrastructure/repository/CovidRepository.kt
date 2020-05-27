package dev.jakal.pandemicwatch.infrastructure.repository

import androidx.room.withTransaction
import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.model.GlobalHistory
import dev.jakal.pandemicwatch.domain.model.GlobalStats
import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.database.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStore
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.*
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service.NovelCovidAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CovidRepository(
    private val apiService: NovelCovidAPIService,
    private val database: CovidDatabase,
    private val keyValueStore: CovidKeyValueStore
) {

    // global stats
    fun getGlobalStatsObservable(): Flow<GlobalStats> =
        keyValueStore.globalStatsObservable.map { it.toDomain() }

    suspend fun updateGlobalStats(): GlobalStats {
        val globalStatsNetwork = apiService.getGlobalStats()
        keyValueStore.globalStats = globalStatsNetwork.toEntity()
        return globalStatsNetwork.toDomain()
    }

    // global history
    fun getGlobalHistoryObservable(): Flow<GlobalHistory> =
        keyValueStore.globalHistoryObservable.map { it.toDomain() }

    suspend fun updateGlobalHistory(): GlobalHistory {
        val timelineNetwork = apiService.getGlobalHistorical()
        keyValueStore.globalHistory = timelineNetwork.toGlobalHistoryEntity()
        return timelineNetwork.toGlobalHistoryDomain()
    }

    // country
    fun getCountriesObservable(): Flow<List<Country>> =
        database.countryDao().getAll().map { it.toDomain() }

    fun getCountryByNameObservable(countryName: String): Flow<Country> =
        database.countryDao().getByCountryName(countryName).map { it.toDomain() }

    fun getCountriesByNameObservable(countryNames: Set<String>): Flow<List<Country>> =
        database.countryDao().getAllByCountryName(countryNames.toList()).map { it.toDomain() }

    suspend fun updateCountries(): List<Country> {
        val countriesNetwork = apiService.getCountries()
        database.withTransaction {
            database.countryDao().deleteAll()
            database.countryDao().insert(countriesNetwork.toEntity())
        }
        return countriesNetwork.toDomain()
    }

    suspend fun updateCountry(countryName: String): Country {
        val countryNetwork = apiService.getCountry(countryName)
        database.countryDao().insert(countryNetwork.toEntity())
        return countryNetwork.toDomain()
    }

    // country history
    fun getCountryHistoryByNameObservable(countryName: String): Flow<CountryHistory> =
        database.countryHistoryDao().getCountryHistoryAndTimeline(countryName)
            .map { it.toDomain() }

    fun getCountriesHistoryByNameObservable(countryNames: Set<String>): Flow<List<CountryHistory>> =
        database.countryHistoryDao().getCountryHistoryAndTimeline(countryNames.toList())
            .map { it.toDomain() }

    suspend fun updateCountriesHistory(): List<CountryHistory> {
        val historicalNetwork = apiService.getHistorical()
        database.withTransaction {
            database.countryHistoryDao().deleteAll()
            database.timelineDao().deleteAll()
            database.countryHistoryDao().insert(historicalNetwork.toEntity())
            database.timelineDao().insert(historicalNetwork.toTimelineEntity())
        }
        return historicalNetwork.toDomain()
    }

    suspend fun updateCountryHistory(countryName: String): CountryHistory {
        val historicalNetwork = apiService.getHistorical(countryName)
        database.withTransaction {
            database.countryHistoryDao().insert(historicalNetwork.toEntity())
            database.timelineDao().insert(historicalNetwork.toTimelineEntity())
        }
        return historicalNetwork.toDomain()
    }

    // favorites
    fun getFavoriteCountriesNamesObservable(): Flow<Set<String>> =
        keyValueStore.favoriteCountriesObservable

    fun addCountryToFavorites(countryName: String) {
        keyValueStore.favoriteCountries = keyValueStore.favoriteCountries.plus(countryName)
    }

    fun removeCountryFromFavorites(countryName: String) {
        keyValueStore.favoriteCountries = keyValueStore.favoriteCountries.minus(countryName)
    }

    // comparison
    fun getComparisonCountriesNamesObservable(): Flow<Set<String>> =
        keyValueStore.comparisonCountriesObservable

    fun addCountryToComparison(countryName: String) {
        keyValueStore.comparisonCountries = keyValueStore.comparisonCountries.plus(countryName)
    }

    fun removeCountryFromComparison(countryName: String) {
        keyValueStore.comparisonCountries = keyValueStore.comparisonCountries.minus(countryName)
    }

    fun resetComparisonCountries() {
        keyValueStore.comparisonCountries = emptySet()
    }
}