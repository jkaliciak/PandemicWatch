package dev.jakal.pandemicwatch.domain.usecase.overview

import dev.jakal.pandemicwatch.domain.model.GlobalStats
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadGlobalStatsUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, GlobalStats>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<GlobalStats> {
        return covidRepository.getGlobalStatsObservable()
    }
}