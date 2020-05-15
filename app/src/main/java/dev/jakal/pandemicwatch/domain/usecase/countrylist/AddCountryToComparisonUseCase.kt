package dev.jakal.pandemicwatch.domain.usecase.countrylist

import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class AddCountryToComparisonUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<AddCountryToComparisonParameters, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: AddCountryToComparisonParameters) {
        return covidRepository.addCountryToComparison(parameters.countryName)
    }
}

data class AddCountryToComparisonParameters(
    val countryName: String
)