package dev.jakal.pandemicwatch.infrastructure.keyvaluestore

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoricalEntity
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
    private var _globalHistorical: GlobalHistoricalEntity? by moshiPref("key_global_historical")
    private var _favoriteCountries: Set<String> by stringSetPref(
        "key_favorite_countries",
        mutableSetOf()
    )
    private val scope = CoroutineScope(defaultDispatcher)
    private val globalStatsChannel = ConflatedBroadcastChannel<GlobalStatsEntity>()
    private val globalHistoricalChannel = ConflatedBroadcastChannel<GlobalHistoricalEntity>()
    private val favoriteCountriesChannel = ConflatedBroadcastChannel<Set<String>>()

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

    override var globalHistorical: GlobalHistoricalEntity?
        get() {
            return _globalHistorical
        }
        set(value) {
            scope.launch {
                value?.let { globalHistoricalChannel.send(it) }
            }
            _globalHistorical = value
        }

    override val globalHistoricalObservable: Flow<GlobalHistoricalEntity>
        get() = globalHistoricalChannel.asFlow()

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

    override val sharedPreferences: SharedPreferences

    init {
        this.moshi = moshi
        sharedPreferences = context.applicationContext.getSharedPreferences(
            "covid_prefs",
            Context.MODE_PRIVATE
        )
        scope.launch {
            globalStats?.let { globalStatsChannel.send(it) }
            globalHistorical?.let { globalHistoricalChannel.send(it) }
            favoriteCountriesChannel.send(favoriteCountries)
        }
    }

    internal fun cleanup() {
        scope.cancel()
        globalStatsChannel.close()
    }
}


