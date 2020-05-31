package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryParameters
import dev.jakal.pandemicwatch.domain.usecase.countryDetails.UpdateCountryUseCase
import dev.jakal.pandemicwatch.infrastructure.database.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class UpdateCountryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateCountryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateCountryUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val country = TestUtils.createCountryEntity("Poland").toDomain()
                coEvery { repository.updateCountry(any()) } returns country

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(UpdateCountryParameters("Poland"))
                        result shouldBe Result.Success(country)
                    }
                }

                "call get repository.updateCountry()" {
                    coVerify { repository.updateCountry(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateCountry() throws exception" should {
                coEvery { repository.updateCountry(any()) } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(UpdateCountryParameters("Poland"))
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateCountry()" {
                    coVerify { repository.updateCountry(any()) }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}