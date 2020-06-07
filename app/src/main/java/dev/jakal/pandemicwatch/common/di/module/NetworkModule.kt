package dev.jakal.pandemicwatch.common.di.module

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service.NovelCovidAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 10L

/**
 * Module for networking dependencies like api services, tools
 */
val networkModule = module {
    single { Moshi.Builder().build() }
    single {
        HttpLoggingInterceptor()
//            .apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            }
    }
    single {
        OkHttpClient.Builder()
            .connectTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .readTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<ChuckerInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja/")
            .client(get())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    get()
                )
            )
            .build()
    }
    single { get<Retrofit>().create(NovelCovidAPIService::class.java) }
    single {
        ChuckerCollector(
            context = get(),
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_DAY
        )
    }
    single {
        ChuckerInterceptor(
            context = get(),
            collector = get(),
            maxContentLength = 250000L,
            headersToRedact = setOf("Auth-Token")
        )
    }
}