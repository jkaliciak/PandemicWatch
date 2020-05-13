package dev.jakal.pandemicwatch.domain.usecase.overview

import dev.jakal.pandemicwatch.domain.model.GlobalHistorical
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadGlobalHistoricalUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, GlobalHistorical>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<GlobalHistorical> {
        return covidRepository.getGlobalHistoricalObservable()
    }
}