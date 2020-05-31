package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.infrastructure.database.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class UpdateCountriesUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateCountriesUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateCountriesUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val countries = listOf(
                    TestUtils.createCountryEntity("Poland"),
                    TestUtils.createCountryEntity("Germany")
                ).toDomain()
                coEvery { repository.updateCountries() } returns countries

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        result shouldBe Result.Success(countries)
                    }
                }

                "call get repository.updateCountries()" {
                    coVerify { repository.updateCountries() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateCountries() throws exception" should {
                coEvery { repository.updateCountries() } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateCountries()" {
                    coVerify { repository.updateCountries() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}