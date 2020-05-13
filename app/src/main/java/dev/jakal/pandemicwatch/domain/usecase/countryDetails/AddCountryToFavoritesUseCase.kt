package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class AddCountryToFavoritesUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<AddCountryToFavoritesParameters, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: AddCountryToFavoritesParameters) {
        return covidRepository.addCountryToFavorites(parameters.countryName)
    }
}

data class AddCountryToFavoritesParameters(
    val countryName: String
)