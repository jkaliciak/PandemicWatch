package dev.jakal.pandemicwatch

import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.GlobalStatsNetwork
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service.NovelCovidAPIService
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Exploratory tests for Novel Covid API https://github.com/NovelCOVID/API
 */
class NovelCovidAPIServiceTest : WordSpec() {

    init {
        "NovelCovidAPIService" When {
            val service = Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
                .create(NovelCovidAPIService::class.java)

            "Called get global stats" should {
                runBlocking {
                    val response = service.getGlobalStats()

                    "response parsed correctly to GlobalStatsNetwork" {
                        response is GlobalStatsNetwork
                    }
                }
            }

            "Called get countries" should {
                runBlocking {
                    val response = service.getCountries()

                    "response is not empty" {
                        response.isEmpty() shouldBe false
                    }
                }
            }

            "Called get country for Poland" should {
                runBlocking {
                    val response = service.getCountry("Poland")

                    "response is for Poland" {
                        response.country shouldBe "Poland"
                    }
                }
            }

            "Called get historical data for Poland" should {
                runBlocking {
                    val response = service.getHistorical()

                    "response is not empty" {
                        response.isEmpty() shouldBe false
                    }
                }
            }
        }
    }
}