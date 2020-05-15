package dev.jakal.pandemicwatch.infrastructure.keyvaluestore

import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoricalEntity
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity
import kotlinx.coroutines.flow.Flow

interface CovidKeyValueStore {

    var globalStats: GlobalStatsEntity?

    val globalStatsObservable: Flow<GlobalStatsEntity>

    var globalHistorical: GlobalHistoricalEntity?

    val globalHistoricalObservable: Flow<GlobalHistoricalEntity>

    var favoriteCountries: Set<String>

    val favoriteCountriesObservable: Flow<Set<String>>

    var comparisonCountries: Set<String>

    val comparisonCountriesObservable: Flow<Set<String>>
}