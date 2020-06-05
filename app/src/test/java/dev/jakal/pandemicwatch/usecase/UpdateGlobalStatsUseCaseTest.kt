package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class UpdateGlobalStatsUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateGlobalStatsUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateGlobalStatsUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val globalStats = TestUtils.createGlobalStatsEntity().toDomain()
                coEvery { repository.updateGlobalStats() } returns globalStats

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        result shouldBe Result.Success(globalStats)
                    }
                }

                "call get repository.updateGlobalStats()" {
                    coVerify { repository.updateGlobalStats() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateGlobalStats() throws exception" should {
                coEvery { repository.updateGlobalStats() } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateGlobalStats()" {
                    coVerify { repository.updateGlobalStats() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}