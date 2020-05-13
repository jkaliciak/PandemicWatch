package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.CountryHistorical
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadCountryHistoricalUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<LoadCountryHistoricalParameters, CountryHistorical>(defaultDispatcher) {

    override fun execute(parameters: LoadCountryHistoricalParameters): Flow<CountryHistorical> {
        return covidRepository.getObservableHistorical(parameters.countryName)
    }
}

data class LoadCountryHistoricalParameters(
    val countryName: String
)