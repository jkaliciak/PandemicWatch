package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countrylist.LoadCountriesUseCase
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

class LoadCountriesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadCountriesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = LoadCountriesUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase when favorites is empty" should {
                val countryNames = setOf<String>()
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesObservable() } returns MutableStateFlow(
                    countries
                )

                "emit Result.Success with 'Poland' not flagged as favorite" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data == countries }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase when just 'Poland' is in favorites" should {
                val countryNames = setOf("Poland")
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountriesObservable() } returns MutableStateFlow(
                    countries
                )
                val expectedCountries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(true),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )

                "emit Result.Success with list of Country and 'Poland' flagged as favorite" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue {
                                it is Result.Success && it.data.any { it.country == "Poland" && it.favorite }
                                        && it.data.any { it.country == "Germany" && !it.favorite }
                            }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getFavoriteCountriesNamesObservable() throws exception" should {
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland").toDomain(),
                    TestUtils.createCountryEntity("Germany").toDomain()
                )
                every { repository.getFavoriteCountriesNamesObservable() } returns flow { throw NullPointerException() }
                every { repository.getCountriesObservable() } returns MutableStateFlow(countries)

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getCountriesObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
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

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountriesObservable()" {
                    coVerify { repository.getCountriesObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}