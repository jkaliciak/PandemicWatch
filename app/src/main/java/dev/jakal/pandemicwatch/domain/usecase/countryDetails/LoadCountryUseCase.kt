package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadCountryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<LoadCountryParameters, Country>(defaultDispatcher) {

    override fun execute(parameters: LoadCountryParameters): Flow<Country> {
        return covidRepository.getObservableCountry(parameters.countryName)
    }
}

data class LoadCountryParameters(
    val countryName: String
)