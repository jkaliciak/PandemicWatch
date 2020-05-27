package dev.jakal.pandemicwatch.domain.usecase.countrylist

import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.domain.usecase.FlowUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LoadCountriesUseCase(
    private val repository: CovidRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Country>>(defaultDispatcher) {

    override fun execute(parameters: Unit): Flow<List<Country>> {
        return repository.getCountriesObservable()
            .combine(repository.getFavoriteCountriesNamesObservable()) { countries, favorites ->
                countries.map { it.copy(favorite = favorites.contains(it.country)) }
            }
    }
}