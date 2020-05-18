package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountryHistoryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<UpdateCountryHistoryParameters, CountryHistory>(defaultDispatcher) {

    override suspend fun execute(parameters: UpdateCountryHistoryParameters): CountryHistory {
        return covidRepository.updateCountryHistory(parameters.countryName)
    }
}

data class UpdateCountryHistoryParameters(
    val countryName: String
)