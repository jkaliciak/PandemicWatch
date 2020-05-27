package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadCountryHistoryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<LoadCountryHistoryParameters, CountryHistory>(defaultDispatcher) {

    override fun execute(parameters: LoadCountryHistoryParameters): Flow<CountryHistory> {
        return covidRepository.getCountryHistoryByNameObservable(parameters.countryName)
    }
}

data class LoadCountryHistoryParameters(
    val countryName: String
)