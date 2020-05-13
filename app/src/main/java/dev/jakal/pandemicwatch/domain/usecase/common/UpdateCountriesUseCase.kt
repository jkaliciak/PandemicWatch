package dev.jakal.pandemicwatch.domain.usecase.common

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountriesUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, List<Country>>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): List<Country> {
        return covidRepository.updateCountries()
    }
}