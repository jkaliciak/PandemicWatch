package dev.jakal.pandemicwatch.presentation.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jakal.pandemicwatch.domain.model.toPresentation
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.overview.LoadGlobalHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.overview.LoadGlobalStatsUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val updateGlobalStatsUseCase: UpdateGlobalStatsUseCase,
    private val loadGlobalStatsUseCase: LoadGlobalStatsUseCase,
    private val updateGlobalHistoricalUseCase: UpdateGlobalHistoricalUseCase,
    private val loadGlobalHistoricalUseCase: LoadGlobalHistoricalUseCase
) : ViewModel() {

    val globalStats: LiveData<GlobalStatsPresentation>
        get() = _globalStats
    private val _globalStats = MutableLiveData<GlobalStatsPresentation>()

    val globalHistorical: LiveData<GlobalHistoricalPresentation>
        get() = _globalHistorical
    private val _globalHistorical = MutableLiveData<GlobalHistoricalPresentation>()

    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    val initializing: LiveData<Boolean>
        get() = _initializing
    private val _initializing = MutableLiveData(true)

    init {
        refreshGlobalStats()

        viewModelScope.launch {
            loadGlobalStatsUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _globalStats.value = it.data.toPresentation()
                        _initializing.value = false
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
        viewModelScope.launch {
            loadGlobalHistoricalUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _globalHistorical.value = it.data.toPresentation()
                        _initializing.value = false
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
    }

    internal fun refreshGlobalStats() {
        viewModelScope.launch {
            _refreshing.value = true
            updateGlobalStatsUseCase(Unit).onError {
                handleError(it.exception)
            }
            updateGlobalHistoricalUseCase(Unit).onError {
                handleError(it.exception)
            }
            _refreshing.value = false
        }
    }

    private fun handleError(throwable: Exception) {
        // TODO handle ui error indication
    }
}