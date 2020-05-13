package dev.jakal.pandemicwatch.presentation.countrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jakal.pandemicwatch.domain.model.toPresentation
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.countrylist.LoadCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val updateCountryListUseCase: UpdateCountriesUseCase,
    private val loadCountriesUseCase: LoadCountriesUseCase
) : ViewModel() {

    val countries: LiveData<CountryListPresentation>
        get() = _countries
    private val _countries = MutableLiveData<CountryListPresentation>()

    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    val initializing: LiveData<Boolean>
        get() = _initializing
    private val _initializing = MutableLiveData(true)

    init {
        refreshCountries()

        viewModelScope.launch {
            loadCountriesUseCase(Unit).collect {
                when (it) {
                    // TODO CountriesPresentation contains domain model Country ???
                    is Result.Success -> {
                        _countries.value = it.data.toPresentation()
                        _initializing.value = false
                    }
                    is Result.Error -> handleError(it.exception)
                }
            }
        }
    }

    internal fun refreshCountries() {
        viewModelScope.launch {
            _refreshing.value = true
            updateCountryListUseCase(Unit).onError {
                handleError(it.exception)
            }
            _refreshing.value = false
        }
    }

    private fun handleError(throwable: Exception) {
        // TODO handle ui error indication
    }
}