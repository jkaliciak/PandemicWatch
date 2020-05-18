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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    fun getObservableCountries(): Flow<List<Country>> =
        database.countryDao().getAll()
            .combine(keyValueStore.favoriteCountriesObservable) { countries, favorites ->
                countries.map { it.toDomain(favorites.contains(it.country)) }
            }

    fun getObservableCountry(countryName: String): Flow<Country> =
        database.countryDao().getByCountryName(countryName)
            .combine(keyValueStore.favoriteCountriesObservable) { country, favorites ->
                country.toDomain(favorites.contains(country.country))
            }

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
    fun getObservableHistory(countryName: String): Flow<CountryHistory> =
        database.countryHistoryDao().getCountryHistoryAndTimeline(countryName)
            .map { it.toDomain() }

    fun getObservableHistory(): Flow<List<CountryHistory>> =
        database.countryHistoryDao().getAllCountryHistoryAndTimeline()
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
    fun addCountryToFavorites(countryName: String) {
        val favoriteCountries = keyValueStore.favoriteCountries
        if (!favoriteCountries.contains(countryName)) {
            keyValueStore.favoriteCountries = favoriteCountries.plus(countryName).toSet()
        }
    }

    fun removeCountryFromFavorites(countryName: String) {
        val favoriteCountries = keyValueStore.favoriteCountries
        if (favoriteCountries.contains(countryName)) {
            keyValueStore.favoriteCountries = favoriteCountries.filter { it != countryName }.toSet()
        }
    }

    // comparison
    fun getObservableComparisonCountries(): Flow<List<Country>> =
        keyValueStore.comparisonCountriesObservable.flatMapLatest {
            database.countryDao().getAllByCountryName(it.toList())
        }.map { it.toDomain() }

    fun getObservableAvailableComparisonCountries(): Flow<List<Country>> =
        database.countryDao().getAll()
            .combine(keyValueStore.comparisonCountriesObservable) { countries, comparisonCountries ->
                countries.filter { !comparisonCountries.contains(it.country) }
            }.map { it.toDomain() }

    fun getObservableComparisonCountriesHistory(): Flow<List<CountryHistory>> =
        keyValueStore.comparisonCountriesObservable.flatMapLatest {
            database.countryHistoryDao().getCountryHistoryAndTimeline(it.toList())
        }.map { it.toDomain() }

    fun addCountryToComparison(countryName: String) {
        val comparisonCountries = keyValueStore.comparisonCountries
        if (!comparisonCountries.contains(countryName)) {
            keyValueStore.comparisonCountries = comparisonCountries.plus(countryName)
        }
    }

    fun removeCountryFromComparison(countryName: String) {
        val comparisonCountries = keyValueStore.comparisonCountries
        if (comparisonCountries.contains(countryName)) {
            keyValueStore.comparisonCountries =
                comparisonCountries.filter { it != countryName }.toSet()
        }
    }

    fun resetComparisonCountries() {
        keyValueStore.comparisonCountries = emptySet()
    }
}