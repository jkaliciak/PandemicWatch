package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countrylist.ResetComparisonCountriesUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class ResetComparisonCountriesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "ResetComparisonCountriesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = ResetComparisonCountriesUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                every { repository.resetComparisonCountries() } just Runs

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        result shouldBe Result.Success(Unit)
                    }
                }

                "call get repository.resetComparisonCountries()" {
                    coVerify { repository.resetComparisonCountries() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.resetComparisonCountries() throws exception" should {
                every { repository.resetComparisonCountries() } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.resetComparisonCountries()" {
                    coVerify { repository.resetComparisonCountries() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}