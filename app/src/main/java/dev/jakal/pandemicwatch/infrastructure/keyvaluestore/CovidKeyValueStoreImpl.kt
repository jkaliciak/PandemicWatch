package dev.jakal.pandemicwatch.infrastructure.keyvaluestore

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoryEntity
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity
import hu.autsoft.krate.Krate
import hu.autsoft.krate.intPref
import hu.autsoft.krate.moshi.moshi
import hu.autsoft.krate.moshi.moshiPref
import hu.autsoft.krate.stringSetPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class CovidKeyValueStoreImpl(
    context: Context,
    moshi: Moshi
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
    private var globalStatsStateFlow = MutableStateFlow<GlobalStatsEntity?>(null)
    private var globalHistoryStateFlow = MutableStateFlow<GlobalHistoryEntity?>(null)
    private var favoriteCountriesStateFlow: MutableStateFlow<Set<String>>
    private var comparisonCountriesStateFlow: MutableStateFlow<Set<String>>

    override var globalStats: GlobalStatsEntity?
        get() {
            return _globalStats
        }
        set(value) {
            globalStatsStateFlow.value = value
            _globalStats = value
        }

    override val globalStatsObservable: Flow<GlobalStatsEntity>
        get() = globalStatsStateFlow.filterNotNull()

    override var globalHistory: GlobalHistoryEntity?
        get() {
            return _globalHistory
        }
        set(value) {
            globalHistoryStateFlow.value = value
            _globalHistory = value
        }

    override val globalHistoryObservable: Flow<GlobalHistoryEntity>
        get() = globalHistoryStateFlow.filterNotNull()

    override var favoriteCountries: Set<String>
        get() {
            return _favoriteCountries
        }
        set(value) {
            favoriteCountriesStateFlow.value = value
            _favoriteCountries = value
        }

    override val favoriteCountriesObservable: Flow<Set<String>>
        get() = favoriteCountriesStateFlow

    override var comparisonCountries: Set<String>
        get() {
            return _comparisonCountries
        }
        set(value) {
            comparisonCountriesStateFlow.value = value
            _comparisonCountries = value
        }

    override val comparisonCountriesObservable: Flow<Set<String>>
        get() = comparisonCountriesStateFlow

    override var nightMode: Int by intPref("key_nightmode", AppCompatDelegate.getDefaultNightMode())

    override val sharedPreferences: SharedPreferences

    init {
        this.moshi = moshi
        sharedPreferences = context.applicationContext.getSharedPreferences(
            "covid_prefs",
            Context.MODE_PRIVATE
        )
        globalStatsStateFlow.value = globalStats
        globalHistoryStateFlow.value = globalHistory
        favoriteCountriesStateFlow = MutableStateFlow(favoriteCountries)
        comparisonCountriesStateFlow = MutableStateFlow(comparisonCountries)
    }
}


