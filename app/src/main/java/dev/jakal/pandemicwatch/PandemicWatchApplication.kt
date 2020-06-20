package dev.jakal.pandemicwatch

import android.app.Application
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.jakal.pandemicwatch.common.di.allModules
import dev.jakal.pandemicwatch.infrastructure.workmanager.UpdateAllDataWorker.Companion.enqueueUpdateDataWorker
import dev.jakal.pandemicwatch.presentation.common.ThemeHelper
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class PandemicWatchApplication : Application() {

    private val loggingTree: Timber.Tree by inject()
    private val workManager: WorkManager by inject()
    private val themeHelper: ThemeHelper by inject()

    override fun onCreate() {
        super.onCreate()

        configureDependencyInjection()
        configureDateTime()
        configureLogging()
        configureWorkManager()
        configureDayNightMode()
    }

    private fun configureDependencyInjection() {
        startKoin {
            androidLogger()
            androidContext(this@PandemicWatchApplication)
            fragmentFactory()
            modules(allModules)
        }
    }

    private fun configureDateTime() {
        AndroidThreeTen.init(this)
    }

    private fun configureLogging() {
        // TODO configure release and debug Timber trees
        Timber.plant(loggingTree)
    }

    private fun configureWorkManager() {
        workManager.enqueueUpdateDataWorker()
    }

    private fun configureDayNightMode() {
        themeHelper.initDefaultNightMode()
    }
}
