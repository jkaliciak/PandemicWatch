package dev.jakal.pandemicwatch.domain.usecase.overview

import dev.jakal.pandemicwatch.domain.model.GlobalHistory
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class LoadGlobalHistoryUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, GlobalHistory>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<GlobalHistory> {
        return covidRepository.getGlobalHistoryObservable()
    }
}