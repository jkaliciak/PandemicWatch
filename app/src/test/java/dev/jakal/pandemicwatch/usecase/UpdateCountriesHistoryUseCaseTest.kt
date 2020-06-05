package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoryUseCase
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

class UpdateCountriesHistoryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateCountriesHistoryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateCountriesHistoryUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val countriesHistory = listOf(
                    TestUtils.createCountryHistory(
                        "Poland",
                        listOf(
                            TestUtils.createTimelineEntity(1, "Poland", TimelineType.CASE),
                            TestUtils.createTimelineEntity(2, "Poland", TimelineType.RECOVERY)
                        ).toDomain()
                    ),
                    TestUtils.createCountryHistory(
                        "Germany",
                        listOf(
                            TestUtils.createTimelineEntity(3, "Germany", TimelineType.CASE),
                            TestUtils.createTimelineEntity(4, "Germany", TimelineType.RECOVERY)
                        ).toDomain()
                    )
                )
                coEvery { repository.updateCountriesHistory() } returns countriesHistory

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        result shouldBe Result.Success(countriesHistory)
                    }
                }

                "call get repository.updateCountriesHistory()" {
                    coVerify { repository.updateCountriesHistory() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateCountriesHistory() throws exception" should {
                coEvery { repository.updateCountriesHistory() } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateCountriesHistory()" {
                    coVerify { repository.updateCountriesHistory() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}