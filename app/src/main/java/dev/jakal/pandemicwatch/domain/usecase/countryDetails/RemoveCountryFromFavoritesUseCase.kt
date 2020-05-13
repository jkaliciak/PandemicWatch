package dev.jakal.pandemicwatch.domain.usecase.countryDetails

import dev.jakal.pandemicwatch.domain.usecase.SuspendUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import kotlinx.coroutines.CoroutineDispatcher

class RemoveCountryFromFavoritesUseCase(
    private val covidRepository: CovidRepository,
    defaultDispatcher: CoroutineDispatcher
) : SuspendUseCase<RemoveCountryFromFavoritesParameters, Unit>(defaultDispatcher) {

    override suspend fun execute(parameters: RemoveCountryFromFavoritesParameters) {
        return covidRepository.removeCountryFromFavorites(parameters.countryName)
    }
}

data class RemoveCountryFromFavoritesParameters(
    val countryName: String
)