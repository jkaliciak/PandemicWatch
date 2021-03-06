package dev.jakal.pandemicwatch.domain.usecase.comparison

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class LoadComparisonCountriesUseCase(
    private val repository: CovidRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Country>>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<List<Country>> {
        return repository.getComparisonCountriesNamesObservable().flatMapLatest {
            repository.getCountriesByNameObservable(it)
        }
    }
}