package dev.jakal.pandemicwatch.common.di.module

import androidx.room.Room
import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStore
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStoreImpl
import org.koin.dsl.module

/**
 * Module for data dependencies like database, key value store
 */
val dataModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CovidDatabase::class.java,
            "covid-database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single<CovidKeyValueStore> {
        CovidKeyValueStoreImpl(
            get(),
            get()
        )
    }
}