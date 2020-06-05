package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryHistoryParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryHistoryUseCase
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineType
import dev.jakal.pandemicwatch.infrastructure.database.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class UpdateCountryHistoryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateCountryHistoryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateCountryHistoryUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val countryHistory = TestUtils.createCountryHistory(
                    "Poland",
                    listOf(
                        TestUtils.createTimelineEntity(1, "Poland", TimelineType.CASE),
                        TestUtils.createTimelineEntity(2, "Poland", TimelineType.RECOVERY)
                    ).toDomain()
                )
                coEvery { repository.updateCountryHistory(any()) } returns countryHistory

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(UpdateCountryHistoryParameters("Poland"))
                        result shouldBe Result.Success(countryHistory)
                    }
                }

                "call get repository.updateCountryHistory()" {
                    coVerify { repository.updateCountryHistory(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateCountryHistory() throws exception" should {
                coEvery { repository.updateCountryHistory(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(UpdateCountryHistoryParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateCountryHistory()" {
                    coVerify { repository.updateCountryHistory(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}