package dev.jakal.pandemicwatch.presentation.comparison

import androidx.lifecycle.*
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadAvailableComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryHistoryParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This is a shared viewmodel
 */
class ComparisonViewModel(
    private val loadComparisonCountriesUseCase: LoadComparisonCountriesUseCase,
    private val loadAvailableComparisonCountriesUseCase: LoadAvailableComparisonCountriesUseCase,
    private val loadComparisonCountriesHistoryUseCase: LoadComparisonCountriesHistoryUseCase,
    private val addCountryToComparisonUseCase: AddCountryToComparisonUseCase,
    private val removeCountryFromComparisonUseCase: RemoveCountryFromComparisonUseCase,
    private val resetComparisonCountriesUseCase: ResetComparisonCountriesUseCase,
    private val updateCountryHistoryUseCase: UpdateCountryHistoryUseCase,
    private val handle: SavedStateHandle
) : ViewModel() {

    val comparison: LiveData<ComparisonPresentation>
        get() = _comparison
    private val _comparison = MutableLiveData(ComparisonPresentation())

    val searchQuery: LiveData<String?>
        get() = _searchQuery
    private val _searchQuery = handle.getLiveData<String?>(KEY_SEARCH_QUERY)

    init {
        viewModelScope.launch {
            loadComparisonCountriesUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _comparison.value = comparison.value?.copy(
                            comparisonCountries = it.data
                        )
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
        viewModelScope.launch {
            loadComparisonCountriesHistoryUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _comparison.value = comparison.value?.copy(
                            comparisonCountriesHistory = it.data
                        )
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
        viewModelScope.launch {
            loadAvailableComparisonCountriesUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _comparison.value = comparison.value?.copy(
                            availableCountries = it.data
                        )
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
    }

    internal fun addCountryToComparison(countryName: String) {
        viewModelScope.launch {
            addCountryToComparisonUseCase(
                AddCountryToComparisonParameters(countryName)
            ).onError {
                handleError(it.exception)
            }
        }
        viewModelScope.launch {
            updateCountryHistoryUseCase(
                UpdateCountryHistoryParameters(countryName)
            ).onError {
                handleError(it.exception)
            }
        }
    }

    internal fun removeCountryFromComparison(countryName: String) {
        viewModelScope.launch {
            removeCountryFromComparisonUseCase(
                RemoveCountryFromComparisonParameters(countryName)
            ).onError {
                handleError(it.exception)
            }
        }
    }

    internal fun resetComparisonCountries() {
        viewModelScope.launch {
            resetComparisonCountriesUseCase(Unit).onError {
                handleError(it.exception)
            }
        }
    }

    internal fun search(query: String?) {
        handle.set(KEY_SEARCH_QUERY, query)
    }

    private fun handleError(throwable: Exception) {
        // TODO handle ui error indication
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "key_search_query"
    }
}