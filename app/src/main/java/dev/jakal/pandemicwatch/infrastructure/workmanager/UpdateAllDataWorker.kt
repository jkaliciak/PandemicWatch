package dev.jakal.pandemicwatch.infrastructure.workmanager

import android.content.Context
import androidx.work.*
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit
import dev.jakal.pandemicwatch.domain.result.Result as UseCaseResult

class UpdateAllDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val updateGlobalStatsUseCase: UpdateGlobalStatsUseCase by inject()
    private val updateGlobalHistoricalUseCase: UpdateGlobalHistoricalUseCase by inject()
    private val updateCountriesUseCase: UpdateCountriesUseCase by inject()
    private val updateCountriesHistoricalUseCase: UpdateCountriesHistoricalUseCase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        // TODO maybe enqueue a separate worker for each update usecase
        val results = listOf(
            updateGlobalStatsUseCase(Unit),
            updateGlobalHistoricalUseCase(Unit),
            updateCountriesUseCase(Unit),
            updateCountriesHistoricalUseCase(Unit)
        )
        if (results.any { it is UseCaseResult.Error }) {
            Result.retry()
        } else {
            Result.success()
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