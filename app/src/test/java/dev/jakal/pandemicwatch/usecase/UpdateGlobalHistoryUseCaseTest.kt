package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoryUseCase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.CoroutineTestRule
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

class UpdateGlobalHistoryUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "UpdateGlobalHistoryUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = UpdateGlobalHistoryUseCase(
                repository,
                coroutineTestRule.testDispatcher
            )

            "invoked usecase" should {
                val globalHistory = TestUtils.createGlobalHistoryEntity().toDomain()
                coEvery { repository.updateGlobalHistory() } returns globalHistory

                "return Result.Success" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        result shouldBe Result.Success(globalHistory)
                    }
                }

                "call get repository.updateGlobalHistory()" {
                    coVerify { repository.updateGlobalHistory() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.updateGlobalHistory() throws exception" should {
                coEvery { repository.updateGlobalHistory() } throws NullPointerException()

                "return Result.Error" {
                    runBlockingTest {
                        val result = useCase(Unit)
                        (result is Result.Error && result.exception is NullPointerException) shouldBe true
                    }
                }

                "call get repository.updateGlobalHistory()" {
                    coVerify { repository.updateGlobalHistory() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}