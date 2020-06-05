package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesHistoryUseCase
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

class LoadComparisonCountriesHistoryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadComparisonCountriesHistoryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase =
                LoadComparisonCountriesHistoryUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase" should {
                val countryNames = setOf("Poland", "Germany")
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
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesHistoryByNameObservable(any()) } returns MutableStateFlow(
                    countriesHistory
                )

                "emit Result.Success with list of CountryHistory" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data == countriesHistory }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                "call get repository.getCountriesHistoryByNameObservable()" {
                    coVerify { repository.getCountriesHistoryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getComparisonCountriesNamesObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
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
                every { repository.getComparisonCountriesNamesObservable() } returns flow { throw NullPointerException() }
                every { repository.getCountriesHistoryByNameObservable(any()) } returns MutableStateFlow(
                    countriesHistory
                )

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getCountriesHistoryByNameObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesHistoryByNameObservable(any()) } returns flow { throw NullPointerException() }

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                "call get repository.getCountriesHistoryByNameObservable()" {
                    coVerify { repository.getCountriesHistoryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}