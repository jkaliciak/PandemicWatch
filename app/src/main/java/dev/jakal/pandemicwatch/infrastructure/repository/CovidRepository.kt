package dev.jakal.pandemicwatch.infrastructure.repository

import androidx.room.withTransaction
import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.model.CountryHistorical
import dev.jakal.pandemicwatch.domain.model.GlobalHistorical
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
    suspend fun updateGlobalStats(): GlobalStats {
        val globalStatsNetwork = apiService.getGlobalStats()
        keyValueStore.globalStats = globalStatsNetwork.toEntity()
        return globalStatsNetwork.toDomain()
    }

    suspend fun updateGlobalHistorical(): GlobalHistorical {
        val timelineNetwork = apiService.getGlobalHistorical()
        keyValueStore.globalHistorical = timelineNetwork.toGlobalHistoricalEntity()
        return timelineNetwork.toGlobalHistoricalDomain()
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

    suspend fun updateCountriesHistorical(): List<CountryHistorical> {
        val historicalNetwork = apiService.getHistorical()
        database.withTransaction {
            database.countryHistoricalDao().deleteAll()
            database.timelineDao().deleteAll()
            database.countryHistoricalDao().insert(historicalNetwork.toEntity())
            database.timelineDao().insert(historicalNetwork.toTimelineEntity())
        }
        return historicalNetwork.toDomain()
    }

    suspend fun updateCountryHistorical(countryName: String): CountryHistorical {
        val historicalNetwork = apiService.getHistorical(countryName)
        database.withTransaction {
            database.countryHistoricalDao().insert(historicalNetwork.toEntity())
            database.timelineDao().insert(historicalNetwork.toTimelineEntity())
        }
        return historicalNetwork.toDomain()
    }

    fun getGlobalStatsObservable(): Flow<GlobalStats> =
        keyValueStore.globalStatsObservable.map { it.toDomain() }

    fun getGlobalHistoricalObservable(): Flow<GlobalHistorical> =
        keyValueStore.globalHistoricalObservable.map { it.toDomain() }

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

    fun getObservableHistorical(countryName: String): Flow<CountryHistorical> =
        database.countryHistoricalDao().getCountryHistoricalAndTimeline(countryName)
            .map { it.toDomain() }

    fun getObservableHistorical(): Flow<List<CountryHistorical>> =
        database.countryHistoricalDao().getAllCountryHistoricalAndTimeline()
            .map { it.toDomain() }

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

    fun getObservableComparisonCountries(): Flow<List<Country>> =
        keyValueStore.comparisonCountriesObservable.flatMapLatest {
            database.countryDao().getAllByCountryName(it.toList())
        }.map { it.toDomain() }

    // TODO
//    fun getObservableComparisonHistorical(): Flow<List<CountryHistorical>> =
//        keyValueStore.comparisonCountriesObservable.flatMapLatest {
//            database.countryHistoricalDao().
//        }

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

    fun getObservableAvailableComparisonCountries(): Flow<List<Country>> =
        database.countryDao().getAll()
            .combine(keyValueStore.comparisonCountriesObservable) { countries, comparisonCountries ->
                countries.filter { !comparisonCountries.contains(it.country) }
            }.map { it.toDomain() }
}