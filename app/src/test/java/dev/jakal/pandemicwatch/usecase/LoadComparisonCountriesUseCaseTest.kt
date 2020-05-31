package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadComparisonCountriesUseCase
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

class LoadComparisonCountriesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadComparisonCountriesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase =
                LoadComparisonCountriesUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase" should {
                val countryNames = setOf("Poland", "Germany")
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesByNameObservable(any()) } returns MutableStateFlow(
                    countries
                )

                "emit Result.Success with list of Country" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data == countries }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                "call get repository.getCountriesByNameObservable()" {
                    coVerify { repository.getCountriesByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getComparisonCountriesNamesObservable() throws exception" should {
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getComparisonCountriesNamesObservable() } returns flow { throw NullPointerException() }
                every { repository.getCountriesByNameObservable(any()) } returns MutableStateFlow(
                    countries
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

            "invoked usecase and repository.getCountriesByNameObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesByNameObservable(any()) } returns flow { throw NullPointerException() }

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

                "call get repository.getCountriesByNameObservable()" {
                    coVerify { repository.getCountriesByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}