package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.LoadCountryParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.LoadCountryUseCase
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

class LoadCountryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadCountryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = LoadCountryUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase for 'Poland' when it's not in favorites" should {
                val countryNames = setOf<String>()
                val country = TestUtils.createCountryEntity("Poland").toDomain()
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountryByNameObservable(any()) } returns MutableStateFlow(
                    country
                )

                "emit Result.Success with 'Poland' not flagged as favorite" {
                    runBlockingTest {
                        useCase(LoadCountryParameters("Poland")).test(this) {
                            assertValue { it is Result.Success && it.data == country && !it.data.favorite }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountryByNameObservable()" {
                    coVerify { repository.getCountryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase for 'Poland' when it's in favorites" should {
                val countryNames = setOf("Poland", "Germany")
                val country = TestUtils.createCountryEntity("Poland").toDomain()
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountryByNameObservable(any()) } returns MutableStateFlow(
                    country
                )

                "emit Result.Success with 'Poland' flagged as favorite" {
                    runBlockingTest {
                        useCase(LoadCountryParameters("Poland")).test(this) {
                            assertValue { it is Result.Success && it.data.country == "Poland" && it.data.favorite }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountryByNameObservable()" {
                    coVerify { repository.getCountryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getFavoriteCountriesNamesObservable() throws exception" should {
                val country = TestUtils.createCountryEntity("Poland").toDomain()
                every { repository.getFavoriteCountriesNamesObservable() } returns flow { throw NullPointerException() }
                every { repository.getCountryByNameObservable(any()) } returns MutableStateFlow(
                    country
                )

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(LoadCountryParameters("Poland")).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountryByNameObservable()" {
                    coVerify { repository.getCountryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getCountryByNameObservable() throws exception" should {
                val countryNames = setOf("Poland", "Germany")
                every { repository.getFavoriteCountriesNamesObservable() } returns MutableStateFlow(
                    countryNames
                )
                every { repository.getCountryByNameObservable(any()) } returns flow { throw NullPointerException() }

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(LoadCountryParameters("Poland")).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getFavoriteCountriesNamesObservable()" {
                    coVerify { repository.getFavoriteCountriesNamesObservable() }
                }

                "call get repository.getCountryByNameObservable()" {
                    coVerify { repository.getCountryByNameObservable(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}