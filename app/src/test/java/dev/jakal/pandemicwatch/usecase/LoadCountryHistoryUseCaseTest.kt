package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.LoadCountryHistoryParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.LoadCountryHistoryUseCase
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineType
import dev.jakal.pandemicwatch.infrastructure.database.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import dev.olog.flow.test.observer.test
import io.kotest.core.spec.style.WordSpec
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class LoadCountryHistoryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadCountryHistoryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = LoadCountryHistoryUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase" should {
                val history = TestUtils.createCountryHistory(
                    "Paland",
                    listOf(
                        TestUtils.createTimelineEntity(1, "Poland", TimelineType.CASE),
                        TestUtils.createTimelineEntity(2, "Poland", TimelineType.RECOVERY)
                    ).toDomain()
                )
                every { repository.getCountryHistoryByNameObservable(any()) } returns MutableStateFlow(
                    history
                )

                "emit Result.Success with GlobalHistory" {
                    runBlockingTest {
                        useCase(LoadCountryHistoryParameters("Poland")).test(this) {
                            assertValue { it is Result.Success && it.data == history }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getCountryHistoryByNameObservable()" {
                    coVerify { repository.getCountryHistoryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getCountryHistoryByNameObservable() throws exception" should {
                every { repository.getCountryHistoryByNameObservable(any()) } returns flow { throw NullPointerException() }

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(LoadCountryHistoryParameters("Poland")).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getCountryHistoryByNameObservable()" {
                    coVerify { repository.getCountryHistoryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}