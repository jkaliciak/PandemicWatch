package dev.jakal.pandemicwatch

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.workmanager.UpdateAllDataWorker
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import dev.jakal.pandemicwatch.domain.result.Result as UseCaseResult

@RunWith(AndroidJUnit4::class)
class UpdateAllDataWorkerTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    private lateinit var context: Context
    private lateinit var updateGlobalStatsUseCase: UpdateGlobalStatsUseCase
    private lateinit var updateGlobalHistoryUseCase: UpdateGlobalHistoryUseCase
    private lateinit var updateCountriesUseCase: UpdateCountriesUseCase
    private lateinit var updateCountriesHistoryUseCase: UpdateCountriesHistoryUseCase

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().context

        if (!::updateGlobalStatsUseCase.isInitialized) {
            updateGlobalStatsUseCase = declareMock()
        }
        if (!::updateGlobalHistoryUseCase.isInitialized) {
            updateGlobalHistoryUseCase = declareMock()
        }
        if (!::updateCountriesUseCase.isInitialized) {
            updateCountriesUseCase = declareMock()
        }
        if (!::updateCountriesHistoryUseCase.isInitialized) {
            updateCountriesHistoryUseCase = declareMock()
        }
        
        coEvery { updateGlobalStatsUseCase.invoke(any()) } returns UseCaseResult.Success(
            TestUtils.createGlobalStatsEntity().toDomain()
        )
        coEvery { updateGlobalHistoryUseCase.invoke(any()) } returns UseCaseResult.Success(
            TestUtils.createGlobalHistoryEntity().toDomain()
        )
        coEvery { updateCountriesUseCase.invoke(any()) } returns UseCaseResult.Success(
            emptyList()
        )
        coEvery { updateCountriesHistoryUseCase.invoke(any()) } returns UseCaseResult.Success(
            emptyList()
        )
    }

    @Test
    fun workerShouldReturnSuccessWhenAllUseCasesReturnSuccess() {
        // given
        val worker = TestListenableWorkerBuilder<UpdateAllDataWorker>(context).build()

        // when
        runBlocking {
            val result = worker.doWork()

            // than
            result shouldBe ListenableWorker.Result.success()
            coVerify { updateGlobalStatsUseCase(any()) }
            coVerify { updateGlobalHistoryUseCase(any()) }
            coVerify { updateCountriesUseCase(any()) }
            coVerify { updateCountriesHistoryUseCase(any()) }
        }
        confirmVerified(
            updateGlobalStatsUseCase,
            updateGlobalHistoryUseCase,
            updateCountriesUseCase,
            updateCountriesHistoryUseCase
        )
        clearAllMocks()
    }

    @Test
    fun workerShouldReturnRetryWhenUpdateGlobalStatsUseCaseReturnsError() {
        // given
        val worker = TestListenableWorkerBuilder<UpdateAllDataWorker>(context).build()
        coEvery { updateGlobalStatsUseCase.invoke(any()) } returns UseCaseResult.Error(
            TestUtils.createHttpException(
                404
            )
        )

        // when
        runBlocking {
            val result = worker.doWork()

            // than
            result shouldBe ListenableWorker.Result.retry()
            coVerify { updateGlobalStatsUseCase(any()) }
            coVerify { updateGlobalHistoryUseCase(any()) }
            coVerify { updateCountriesUseCase(any()) }
            coVerify { updateCountriesHistoryUseCase(any()) }
        }
        confirmVerified(
            updateGlobalStatsUseCase,
            updateGlobalHistoryUseCase,
            updateCountriesUseCase,
            updateCountriesHistoryUseCase
        )
        clearAllMocks()
    }

    @Test
    fun workerShouldReturnRetryWhenUpdateGlobalHistoryUseCaseReturnsError() {
        // given
        val worker = TestListenableWorkerBuilder<UpdateAllDataWorker>(context).build()
        coEvery { updateGlobalHistoryUseCase.invoke(any()) } returns UseCaseResult.Error(
            TestUtils.createHttpException(
                404
            )
        )

        // when
        runBlocking {
            val result = worker.doWork()

            // than
            result shouldBe ListenableWorker.Result.retry()
            coVerify { updateGlobalStatsUseCase(any()) }
            coVerify { updateGlobalHistoryUseCase(any()) }
            coVerify { updateCountriesUseCase(any()) }
            coVerify { updateCountriesHistoryUseCase(any()) }
        }
        confirmVerified(
            updateGlobalStatsUseCase,
            updateGlobalHistoryUseCase,
            updateCountriesUseCase,
            updateCountriesHistoryUseCase
        )
        clearAllMocks()
    }

    @Test
    fun workerShouldReturnRetryWhenUpdateCountriesUseCaseReturnsError() {
        // given
        val worker = TestListenableWorkerBuilder<UpdateAllDataWorker>(context).build()
        coEvery { updateCountriesUseCase.invoke(any()) } returns UseCaseResult.Error(
            TestUtils.createHttpException(
                404
            )
        )

        // when
        runBlocking {
            val result = worker.doWork()

            // than
            result shouldBe ListenableWorker.Result.retry()
            coVerify { updateGlobalStatsUseCase(any()) }
            coVerify { updateGlobalHistoryUseCase(any()) }
            coVerify { updateCountriesUseCase(any()) }
            coVerify { updateCountriesHistoryUseCase(any()) }
        }
        confirmVerified(
            updateGlobalStatsUseCase,
            updateGlobalHistoryUseCase,
            updateCountriesUseCase,
            updateCountriesHistoryUseCase
        )
        clearAllMocks()
    }

    @Test
    fun workerShouldReturnRetryWhenUpdateCountriesHistoryUseCaseReturnsError() {
        // given
        val worker = TestListenableWorkerBuilder<UpdateAllDataWorker>(context).build()
        coEvery { updateCountriesHistoryUseCase.invoke(any()) } returns UseCaseResult.Error(
            TestUtils.createHttpException(
                404
            )
        )

        // when
        runBlocking {
            val result = worker.doWork()

            // than
            result shouldBe ListenableWorker.Result.retry()
            coVerify { updateGlobalStatsUseCase(any()) }
            coVerify { updateGlobalHistoryUseCase(any()) }
            coVerify { updateCountriesUseCase(any()) }
            coVerify { updateCountriesHistoryUseCase(any()) }
        }
        confirmVerified(
            updateGlobalStatsUseCase,
            updateGlobalHistoryUseCase,
            updateCountriesUseCase,
            updateCountriesHistoryUseCase
        )
        clearAllMocks()
    }
}