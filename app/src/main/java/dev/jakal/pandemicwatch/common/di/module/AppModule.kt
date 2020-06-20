package dev.jakal.pandemicwatch.common.di.module

import androidx.work.WorkManager
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateCountriesUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalHistoryUseCase
import dev.jakal.pandemicwatch.domain.usecase.common.UpdateGlobalStatsUseCase
import dev.jakal.pandemicwatch.infrastructure.logging.AppDebugTree
import dev.jakal.pandemicwatch.presentation.common.ThemeHelper
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import timber.log.Timber

/**
 * Module for core application dependencies
 */
val appModule = module {
    single<Timber.Tree> { AppDebugTree(get()) }
    single { ThemeHelper(get()) }
    single { WorkManager.getInstance(get()) }
    single {
        UpdateGlobalHistoryUseCase(
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
        UpdateCountriesHistoryUseCase(
            get(),
            Dispatchers.Default
        )
    }
}