package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.comparison.LoadAvailableComparisonCountriesUseCase
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

class LoadAvailableComparisonCountriesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadAvailableComparisonCountriesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase =
                LoadAvailableComparisonCountriesUseCase(
                    repository,
                    coroutineTestRule.testDispatcher
                )

            "invoked usecase while 'Poland' is added to comparison" should {
                val countryNames = setOf("Poland")
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getCountriesObservable() } returns MutableStateFlow(countries)
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )

                "emit Result.Success with list of Country without 'Poland'" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data.none { it.country == "Poland" } }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase while comparison is empty" should {
                val countryNames = emptySet<String>()
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getCountriesObservable() } returns MutableStateFlow(countries)
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )

                "emit Result.Success with list of Country without 'Poland'" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data == countries }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
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
                every { repository.getCountriesObservable() } returns MutableStateFlow(countries)

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getCountriesObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
                every { repository.getComparisonCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesObservable() } returns flow { throw NullPointerException() }

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                "call get repository.getComparisonCountriesNamesObservable()" {
                    coVerify { repository.getComparisonCountriesNamesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}