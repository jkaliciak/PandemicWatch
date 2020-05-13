package dev.jakal.pandemicwatch.common.di.module

import androidx.work.WorkManager
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoricalUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import dev.jakal.pandemicwatch.infrastructure.logging.AppDebugTree
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import timber.log.Timber

/**
 * Module for core application dependencies
 */
val appModule = module {
    single<Timber.Tree> {
        AppDebugTree(get())
    }

    single {
        WorkManager.getInstance(get())
    }
    single {
        UpdateGlobalHistoricalUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        UpdateGlobalStatsUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        UpdateCountriesUseCase(
            get(),
            Dispatchers.Default
        )
    }
    single {
        UpdateCountriesHistoricalUseCase(
            get(),
            Dispatchers.Default
        )
    }
}