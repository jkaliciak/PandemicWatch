package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.AddCountryToFavoritesParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.AddCountryToFavoritesUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class AddCountryToFavoritesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "AddCountryToFavoritesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = AddCountryToFavoritesUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                every { repository.addCountryToFavorites(any()) } just Runs

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(AddCountryToFavoritesParameters("Poland"))
                        result shouldBe Result.Success(Unit)
                    }
                }

                "call get repository.addCountryToFavorites()" {
                    coVerify { repository.addCountryToFavorites(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.addCountryToFavorites() throws exception" should {
                every { repository.addCountryToFavorites(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(AddCountryToFavoritesParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.addCountryToFavorites()" {
                    coVerify { repository.addCountryToFavorites(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}