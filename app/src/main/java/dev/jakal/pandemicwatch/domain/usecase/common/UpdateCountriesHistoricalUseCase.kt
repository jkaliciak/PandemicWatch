package dev.jakal.pandemicwatch.domain.usecase.common

import dev.jakal.pandemicwatch.domain.model.CountryHistorical
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountriesHistoricalUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, List<CountryHistorical>>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): List<CountryHistorical> {
        return covidRepository.updateCountriesHistorical()
    }
}