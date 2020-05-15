package dev.jakal.pandemicwatch.domain.usecase.countrylist

import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class ResetComparisonCountriesUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit) {
        return covidRepository.resetComparisonCountries()
    }
}