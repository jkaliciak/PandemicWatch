package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<UpdateCountryParameters, Country>(defaultDispatcher) {

    override suspend fun execute(parameters: UpdateCountryParameters): Country {
        return covidRepository.updateCountry(parameters.countryName)
    }
}

data class UpdateCountryParameters(
    val countryName: String
)