package dev.jakal.pandemicwatch.domain.usecase.common

import dev.jakal.pandemicwatch.domain.model.GlobalHistorical
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateGlobalHistoricalUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, GlobalHistorical>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): GlobalHistorical {
        return covidRepository.updateGlobalHistorical()
    }
}