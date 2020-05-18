package dev.jakal.pandemicwatch.domain.usecase.countrylist

import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class RemoveCountryFromComparisonUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<RemoveCountryFromComparisonParameters, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: RemoveCountryFromComparisonParameters) {
        return covidRepository.removeCountryFromComparison(parameters.countryName)
    }
}

data class RemoveCountryFromComparisonParameters(
    val countryName: String
)