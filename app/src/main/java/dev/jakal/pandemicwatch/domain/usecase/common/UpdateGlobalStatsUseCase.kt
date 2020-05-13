package dev.jakal.pandemicwatch.domain.usecase.common

import dev.jakal.pandemicwatch.domain.model.GlobalStats
import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class UpdateGlobalStatsUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, GlobalStats>(defaultDispatcher) {

    override suspend fun execute(parameters: Unit): GlobalStats {
        return covidRepository.updateGlobalStats()
    }
}