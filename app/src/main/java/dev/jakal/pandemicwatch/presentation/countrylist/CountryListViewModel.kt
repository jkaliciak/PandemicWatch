package dev.jakal.pandemicwatch.presentation.countrylist

import androidx.lifecycle.*
import dev.jakal.pandemicwatch.domain.model.toPresentation
import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.result.onError
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.countrylist.LoadCountriesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val updateCountryListUseCase: UpdateCountriesUseCase,
    private val loadCountriesUseCase: LoadCountriesUseCase,
    private val handle: SavedStateHandle
) : ViewModel() {

    val countries: LiveData<CountryListPresentation>
        get() = _countries
    private val _countries = MutableLiveData<CountryListPresentation>()

    val searchQuery: LiveData<String?>
        get() = _searchQuery
    private val _searchQuery = handle.getLiveData<String?>(KEY_SEARCH_QUERY)

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