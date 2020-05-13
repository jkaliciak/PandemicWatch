package dev.jakal.pandemicwatch.common.di.module

import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import org.koin.dsl.module

/**
 * Module for repositories
 */
val repositoryModule = module {
    single {
        CovidRepository(
            get(),
            get(),
            get()
        )
    }
}