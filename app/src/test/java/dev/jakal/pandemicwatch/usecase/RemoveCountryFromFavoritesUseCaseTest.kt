package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.RemoveCountryFromFavoritesParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.RemoveCountryFromFavoritesUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class RemoveCountryFromFavoritesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "RemoveCountryFromFavoritesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = RemoveCountryFromFavoritesUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                every { repository.removeCountryFromFavorites(any()) } just Runs

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(RemoveCountryFromFavoritesParameters("Poland"))
                        result shouldBe Result.Success(Unit)
                    }
                }

                "call get repository.removeCountryFromFavorites()" {
                    coVerify { repository.removeCountryFromFavorites(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.removeCountryFromFavorites() throws exception" should {
                every { repository.removeCountryFromFavorites(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(RemoveCountryFromFavoritesParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.removeCountryFromFavorites()" {
                    coVerify { repository.removeCountryFromFavorites(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}