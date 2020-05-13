package dev.jakal.pandemicwatch.presentation.countrydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jakal.pandemicwatch.domain.model.toPresentation
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val countryName: String,
    private val updateCountryUseCase: UpdateCountryUseCase,
    private val loadCountryUseCase: LoadCountryUseCase,
    private val updateCountryHistoricalUseCase: UpdateCountryHistoricalUseCase,
    private val loadCountryHistoricalUseCase: LoadCountryHistoricalUseCase,
    private val addCountryToFavoritesUseCase: AddCountryToFavoritesUseCase,
    private val removeCountryFromFavoritesUseCase: RemoveCountryFromFavoritesUseCase
) : ViewModel() {

    val country: LiveData<CountryPresentation>
        get() = _country
    private val _country = MutableLiveData<CountryPresentation>()

    val countryHistorical: LiveData<CountryHistoricalPresentation>
        get() = _countryHistorical
    private val _countryHistorical = MutableLiveData<CountryHistoricalPresentation>()

    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    val initializing: LiveData<Boolean>
        get() = _initializing
    private val _initializing = MutableLiveData(true)

    init {
        refreshCountry()

        viewModelScope.launch {
            loadCountryHistoricalUseCase(LoadCountryHistoricalParameters(countryName))
                .combine(loadCountryUseCase(LoadCountryParameters(countryName))) { country, countryHistorical ->
                    countryHistorical to country
                }.collect {
                    // TODO CountriesPresentation contains domain model Country, CountryHistorical ???
                    val (country, countryHistorical) = it
                    when (country) {
                        is Result.Success -> {
                            _country.value = country.data.toPresentation()
                            _initializing.value = false
                        }
                        is Result.Error -> handleError(country.exception)
                    }
                    when (countryHistorical) {
                        is Result.Success -> {
                            _countryHistorical.value = countryHistorical.data.toPresentation()
                            _initializing.value = false
                        }
                        is Result.Error -> handleError(countryHistorical.exception)
                    }
                }
        }
    }

    internal fun refreshCountry() {
        viewModelScope.launch {
            _refreshing.value = true
            updateCountryUseCase(UpdateCountryParameters(countryName))
                .onError { handleError(it.exception) }
            updateCountryHistoricalUseCase(UpdateCountryHistoricalParameters(countryName))
                .onError { handleError(it.exception) }
            _refreshing.value = false
        }
    }

    internal fun addToFavorites() {
        viewModelScope.launch {
            addCountryToFavoritesUseCase(AddCountryToFavoritesParameters(countryName))
        }
    }

    internal fun removeFromFavorites() {
        viewModelScope.launch {
            removeCountryFromFavoritesUseCase(RemoveCountryFromFavoritesParameters(countryName))
        }
    }

    private fun handleError(throwable: Exception) {
        // TODO handle ui error indication
    }
}