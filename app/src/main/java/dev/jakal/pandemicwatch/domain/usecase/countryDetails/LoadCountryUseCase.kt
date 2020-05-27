package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LoadCountryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<LoadCountryParameters, Country>(defaultDispatcher) {

    override fun execute(parameters: LoadCountryParameters): Flow<Country> {
        return covidRepository.getCountryByNameObservable(parameters.countryName)
            .combine(covidRepository.getFavoriteCountriesNamesObservable()) { country, favorites ->
                country.copy(favorite = favorites.contains(country.country))
            }
    }
}

data class LoadCountryParameters(
    val countryName: String
)