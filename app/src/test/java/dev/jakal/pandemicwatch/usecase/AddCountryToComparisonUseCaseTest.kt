package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countrylist.AddCountryToComparisonParameters
import dev.jakal.pandemicwatch.domain.usecase.countrylist.AddCountryToComparisonUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class AddCountryToComparisonUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "AddCountryToComparisonUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = AddCountryToComparisonUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                every { repository.addCountryToComparison(any()) } just Runs

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(AddCountryToComparisonParameters("Poland"))
                        result shouldBe Result.Success(Unit)
                    }
                }

                "call get repository.addCountryToComparison()" {
                    coVerify { repository.addCountryToComparison(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.addCountryToComparison() throws exception" should {
                every { repository.addCountryToComparison(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(AddCountryToComparisonParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.addCountryToComparison()" {
                    coVerify { repository.addCountryToComparison(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}