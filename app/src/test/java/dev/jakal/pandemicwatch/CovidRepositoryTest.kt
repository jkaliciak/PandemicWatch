package dev.jakal.pandemicwatch

import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStore
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.GlobalStatsNetwork
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service.NovelCovidAPIService
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import io.kotest.core.spec.style.WordSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk

class CovidRepositoryTest : WordSpec() {

    init {

        // TODO test repo
//        "CovidRepository" When {
//            val apiService: NovelCovidAPIService = mockk()
//            val database: CovidDatabase = mockk()
//            val keyValueStore: CovidKeyValueStore = mockk()
//            val repository = CovidRepository(apiService, database, keyValueStore)
//
//            "called updateGlobalStats()" should {
//                coEvery { apiService.getGlobalStats() } returns
//                        GlobalStatsNetwork(
//                            0,
//                            1,
//                            2,
//                            3,
//                            4,
//                            5,
//                            6,
//                            7.0,
//                            8.0,
//                            9,
//                            10,
//                            11,
//                            12.0
//                        )
//
//                "call covidAPIService.getGlobalStats()" {
//                    coVerify { apiService.getGlobalStats() }
//                }
//
//                confirmVerified(apiService, database)
//            }
//        }
    }
}