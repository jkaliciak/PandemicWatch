package dev.jakal.pandemicwatch.infrastructure.keyvaluestore

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoryEntity
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity
import hu.autsoft.krate.Krate
import hu.autsoft.krate.moshi.moshi
import hu.autsoft.krate.moshi.moshiPref
import hu.autsoft.krate.stringSetPref
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class CovidKeyValueStoreImpl(
    context: Context,
    moshi: Moshi,
    defaultDispatcher: CoroutineDispatcher
) : Krate, CovidKeyValueStore {

    private var _globalStats: GlobalStatsEntity? by moshiPref("key_global_stats")
    private var _globalHistory: GlobalHistoryEntity? by moshiPref("key_global_history")
    private var _favoriteCountries: Set<String> by stringSetPref(
        "key_favorite_countries",
        mutableSetOf()
    )
    private var _comparisonCountries: Set<String> by stringSetPref(
        "key_comparison_countries",
        mutableSetOf()
    )
    private val scope = CoroutineScope(defaultDispatcher)
    private val globalStatsChannel = ConflatedBroadcastChannel<GlobalStatsEntity>()
    private val globalHistoryChannel = ConflatedBroadcastChannel<GlobalHistoryEntity>()
    private val favoriteCountriesChannel = ConflatedBroadcastChannel<Set<String>>()
    private val comparisonCountriesChannel = ConflatedBroadcastChannel<Set<String>>()

    override var globalStats: GlobalStatsEntity?
        get() {
            return _globalStats
        }
        set(value) {
            scope.launch {
                value?.let { globalStatsChannel.send(it) }
            }
            _globalStats = value
        }

    override val globalStatsObservable: Flow<GlobalStatsEntity>
        get() = globalStatsChannel.asFlow()

    override var globalHistory: GlobalHistoryEntity?
        get() {
            return _globalHistory
        }
        set(value) {
            scope.launch {
                value?.let { globalHistoryChannel.send(it) }
            }
            _globalHistory = value
        }

    override val globalHistoryObservable: Flow<GlobalHistoryEntity>
        get() = globalHistoryChannel.asFlow()

    override var favoriteCountries: Set<String>
        get() {
            return _favoriteCountries
        }
        set(value) {
            scope.launch {
                favoriteCountriesChannel.send(value)
            }
            _favoriteCountries = value
        }

    override val favoriteCountriesObservable: Flow<Set<String>>
        get() = favoriteCountriesChannel.asFlow()

    override var comparisonCountries: Set<String>
        get() {
            return _comparisonCountries
        }
        set(value) {
            scope.launch {
                comparisonCountriesChannel.send(value)
            }
            _comparisonCountries = value
        }

    override val comparisonCountriesObservable: Flow<Set<String>>
        get() = comparisonCountriesChannel.asFlow()

    override val sharedPreferences: SharedPreferences

    init {
        this.moshi = moshi
        sharedPreferences = context.applicationContext.getSharedPreferences(
            "covid_prefs",
            Context.MODE_PRIVATE
        )
        scope.launch {
            globalStats?.let { globalStatsChannel.send(it) }
            globalHistory?.let { globalHistoryChannel.send(it) }
            favoriteCountriesChannel.send(favoriteCountries)
            comparisonCountriesChannel.send(comparisonCountries)
        }
    }

    internal fun cleanup() {
        scope.cancel()
        globalStatsChannel.close()
        globalHistoryChannel.close()
        favoriteCountriesChannel.close()
        comparisonCountriesChannel.close()
    }
}


