package dev.jakal.pandemicwatch.usecase

import dev.jakal.pandemicwatch.domain.result.Result
import dev.jakal.pandemicwatch.domain.usecase.overview.LoadGlobalStatsUseCase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
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

class LoadGlobalStatsUseCaseTest : WordSpec() {

    @get:Rule
    private val coroutineTestRule = CoroutineTestRule()

    init {
        "LoadGlobalStatsUseCase" When {
            val repository: CovidRepository = mockk()
            val useCase = LoadGlobalStatsUseCase(repository, coroutineTestRule.testDispatcher)

            "invoked usecase" should {
                val history = TestUtils.createGlobalStatsEntity().toDomain()
                every { repository.getGlobalStatsObservable() } returns MutableStateFlow(history)

                "emit Result.Success with GlobalHistory" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Success && it.data == history }
                            assertNotComplete()
                        }
                    }
                }

                "call get repository.getGlobalStatsObservable()" {
                    coVerify { repository.getGlobalStatsObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }

            "invoked usecase and repository.getGlobalStatsObservable() throws exception" should {
                every { repository.getGlobalStatsObservable() } returns flow { throw NullPointerException() }

                "emit Result.Error" {
                    runBlockingTest {
                        useCase(Unit).test(this) {
                            assertValue { it is Result.Error }
                            assertComplete()
                        }
                    }
                }

                "call get repository.getGlobalStatsObservable()" {
                    coVerify { repository.getGlobalStatsObservable() }
                }

                confirmVerified(repository)
                clearAllMocks()
            }
        }
    }
}