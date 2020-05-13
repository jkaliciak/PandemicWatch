package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.CountryHistorical
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountryHistoricalUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<UpdateCountryHistoricalParameters, CountryHistorical>(defaultDispatcher) {

    override suspend fun execute(parameters: UpdateCountryHistoricalParameters): CountryHistorical {
        return covidRepository.updateCountryHistorical(parameters.countryName)
    }
}

data class UpdateCountryHistoricalParameters(
    val countryName: String
)