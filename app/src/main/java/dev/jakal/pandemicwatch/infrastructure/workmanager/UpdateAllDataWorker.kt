package dev.jakal.pandemicwatch.infrastructure.workmanager

import android.content.Context
import androidx.work.*
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit
import dev.jakal.pandemicwatch.domain.result.Result as UseCaseResult

class UpdateAllDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val updateGlobalStatsUseCase: UpdateGlobalStatsUseCase by inject()
    private val updateGlobalHistoryUseCase: UpdateGlobalHistoryUseCase by inject()
    private val updateCountriesUseCase: UpdateCountriesUseCase by inject()
    private val updateCountriesHistoryUseCase: UpdateCountriesHistoryUseCase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val results = listOf(
            updateGlobalStatsUseCase(Unit),
            updateGlobalHistoryUseCase(Unit),
            updateCountriesUseCase(Unit),
            updateCountriesHistoryUseCase(Unit)
        )
        if (results.all { it is UseCaseResult.Success }) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        private const val uniqueWorkName = "update_all_data_worker"

        fun WorkManager.enqueueUpdateDataWorker() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .setRequiresBatteryNotLow(true)
                .build()
            val request = PeriodicWorkRequestBuilder<UpdateAllDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
            this.enqueueUniquePeriodicWork(
                uniqueWorkName,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}