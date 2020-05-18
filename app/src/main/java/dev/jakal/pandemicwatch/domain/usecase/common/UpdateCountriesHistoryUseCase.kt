package dev.jakal.pandemicwatch.domain.usecase.common

import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateCountriesHistoryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, List<CountryHistory>>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): List<CountryHistory> {
        return covidRepository.updateCountriesHistory()
    }
}