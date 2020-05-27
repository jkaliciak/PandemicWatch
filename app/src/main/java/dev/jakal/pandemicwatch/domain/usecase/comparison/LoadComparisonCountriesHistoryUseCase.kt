package dev.jakal.pandemicwatch.domain.usecase.comparison

import dev.jakal.pandemicwatch.domain.model.CountryHistory
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class LoadComparisonCountriesHistoryUseCase(
    private val repository: CovidRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<CountryHistory>>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<List<CountryHistory>> {
        return repository.getComparisonCountriesNamesObservable().flatMapLatest {
            repository.getCountriesHistoryByNameObservable(it)
        }
    }
}