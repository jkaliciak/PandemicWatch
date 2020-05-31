package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countrylist.RemoveCountryFromComparisonParameters
import dev.jakal.pandemicwatch.domain.usecase.countrylist.RemoveCountryFromComparisonUseCase
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class RemoveCountryFromComparisonUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "RemoveCountryFromComparisonUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = RemoveCountryFromComparisonUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                every { repository.removeCountryFromComparison(any()) } just Runs

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(RemoveCountryFromComparisonParameters("Poland"))
                        result shouldBe Result.Success(Unit)
                    }
                }

                "call get repository.removeCountryFromComparison()" {
                    coVerify { repository.removeCountryFromComparison(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.removeCountryFromComparison() throws exception" should {
                every { repository.removeCountryFromComparison(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(RemoveCountryFromComparisonParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.removeCountryFromComparison()" {
                    coVerify { repository.removeCountryFromComparison(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}