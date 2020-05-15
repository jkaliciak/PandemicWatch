package dev.jakal.pandemicwatch.presentation.comparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadAvailableComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This is a shared viewmodel
 */
class ComparisonViewModel(
    private val loadComparisonCountriesUseCase: LoadComparisonCountriesUseCase,
    private val loadAvailableComparisonCountriesUseCase: LoadAvailableComparisonCountriesUseCase,
    private val addCountryToComparisonUseCase: AddCountryToComparisonUseCase,
    private val removeCountryFromComparisonUseCase: RemoveCountryFromComparisonUseCase,
    private val resetComparisonCountriesUseCase: ResetComparisonCountriesUseCase
) : ViewModel() {

    val comparison: LiveData<ComparisonPresentation>
        get() = _comparison
    private val _comparison = MutableLiveData(ComparisonPresentation())

    val initializing: LiveData<Boolean>
        get() = _initializing
    private val _initializing = MutableLiveData(true)

    init {
        viewModelScope.launch {
            loadComparisonCountriesUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        _comparison.value = comparison.value?.copy(
                            comparisonCountries = it.data
                        )
                        _initializing.value = false
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
                        _initializing.value = false
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

    private fun handleError(throwable: Exception) {
        // TODO handle ui error indication
    }
}