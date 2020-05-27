package dev.jakal.pandemicwatch

import androidx.room.withTransaction
import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryDao
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryHistoryDao
import dev.jakal.pandemicwatch.infrastructure.database.dao.TimelineDao
import dev.jakal.pandemicwatch.infrastructure.database.model.*
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStore
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.toDomain
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model.toGlobalHistoryDomain
import dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.service.NovelCovidAPIService
import dev.jakal.pandemicwatch.infrastructure.repository.CovidRepository
import dev.jakal.pandemicwatch.utils.TestUtils
import dev.olog.flow.test.observer.test
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import retrofit2.HttpException

class CovidRepositoryTest : WordSpec() {

    init {
        "CovidRepository" When {
            val apiService: NovelCovidAPIService = mockk()
            val countryDao: CountryDao = mockk()
            val countryHistoryDao: CountryHistoryDao = mockk()
            val timelineDao: TimelineDao = mockk()
            val database: CovidDatabase = mockk()
            val keyValueStore: CovidKeyValueStore = mockk(relaxed = true)
            val repository = CovidRepository(apiService, database, keyValueStore)

            beforeTest {
                every { database.countryDao() } returns countryDao
                every { database.countryHistoryDao() } returns countryHistoryDao
                every { database.timelineDao() } returns timelineDao

                MockKAnnotations.init(this)
                mockkStatic("androidx.room.RoomDatabaseKt")
                val transactionLambda = slot<suspend () -> R>()
                coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
                    transactionLambda.captured.invoke()
                }
            }

            "called getGlobalStatsObservable()" should {
                val entity = TestUtils.createGlobalStatsEntity()
                every { keyValueStore.globalStatsObservable } returns MutableStateFlow(entity)

                "emit GlobalStats" {
                    runBlockingTest {
                        repository.getGlobalStatsObservable().test(this) {
                            assertValue { it == entity.toDomain() }
                            assertNotComplete()
                        }
                    }
                }

                "call get keyValueStore.globalStatsObservable" {
                    coVerify { keyValueStore.globalStatsObservable }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateGlobalStats() and api call succeeded" should {
                val network = TestUtils.createGlobalStatsNetwork()
                coEvery { apiService.getGlobalStats() } returns network

                "return GlobalStats" {
                    repository.updateGlobalStats() shouldBe network.toDomain()
                }

                "call apiService.getGlobalStats()" {
                    coVerify { apiService.getGlobalStats() }
                }

                "call set keyValueStore.globalStats" {
                    coVerify { keyValueStore.globalStats = any() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateGlobalStats() and api call failed" should {
                coEvery { apiService.getGlobalStats() } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateGlobalStats()
                }

                "call apiService.getGlobalStats()" {
                    coVerify { apiService.getGlobalStats() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getGlobalHistoryObservable()" should {
                val entity = TestUtils.createGlobalHistoryEntity()
                every { keyValueStore.globalHistoryObservable } returns MutableStateFlow(entity)

                "emit GlobalHistory" {
                    runBlockingTest {
                        repository.getGlobalHistoryObservable().test(this) {
                            assertValue { it == entity.toDomain() }
                            assertNotComplete()
                        }
                    }
                }

                "call get keyValueStore.globalHistoryObservable" {
                    coVerify { keyValueStore.globalHistoryObservable }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateGlobalHistory() and api call succeeded" should {
                val network = TestUtils.createGlobalHistoryNetwork()
                coEvery { apiService.getGlobalHistorical() } returns network

                "return Timeline" {
                    repository.updateGlobalHistory() shouldBe network.toGlobalHistoryDomain()
                }

                "call apiService.getGlobalHistorical()" {
                    coVerify { apiService.getGlobalHistorical() }
                }

                "call set keyValueStore.globalHistory" {
                    coVerify { keyValueStore.globalHistory = any() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateGlobalHistory() and api call failed" should {
                coEvery { apiService.getGlobalHistorical() } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateGlobalHistory()
                }

                "call apiService.getGlobalHistorical()" {
                    coVerify { apiService.getGlobalHistorical() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getCountriesObservable()" should {
                val entities = listOf(
                    TestUtils.createCountryEntity("Poland"),
                    TestUtils.createCountryEntity("Germany"),
                    TestUtils.createCountryEntity("United Kingdom")
                )
                every { countryDao.getAll() } returns MutableStateFlow(entities)

                "emit list of Country" {
                    runBlockingTest {
                        repository.getCountriesObservable().test(this) {
                            assertValue { it == entities.toDomain() }
                            assertNotComplete()
                        }
                    }
                }

                "call countryDao.getAll()" {
                    coVerify { countryDao.getAll() }
                }

                "call database.countryDao()" {
                    coVerify { database.countryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getCountryByNameObservable()" should {
                val entity = TestUtils.createCountryEntity("Poland")
                every { countryDao.getByCountryName(any()) } returns MutableStateFlow(entity)

                "emit Country" {
                    runBlockingTest {
                        repository.getCountryByNameObservable("Poland").test(this) {
                            assertValue { it == entity.toDomain() }
                            assertNotComplete()
                        }
                    }
                }

                "call countryDao.getByCountryName()" {
                    coVerify { countryDao.getByCountryName(any()) }
                }

                "call database.countryDao()" {
                    coVerify { database.countryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getCountriesByNameObservable()" should {
                val entities = listOf(
                    TestUtils.createCountryEntity("Poland"),
                    TestUtils.createCountryEntity("Germany")
                )
                every { countryDao.getAllByCountryName(any()) } returns MutableStateFlow(entities)

                "emit list of Country" {
                    runBlockingTest {
                        repository.getCountriesByNameObservable(setOf("Poland", "Germany"))
                            .test(this) {
                                assertValue { it == entities.toDomain() }
                                assertNotComplete()
                            }
                    }
                }

                "call countryDao.getCountriesByNameObservable()" {
                    coVerify { countryDao.getAllByCountryName(any()) }
                }

                "call database.countryDao()" {
                    coVerify { database.countryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountries() and api call succeeded" should {
                val networks = listOf(
                    TestUtils.createCountryNetwork("Poland"),
                    TestUtils.createCountryNetwork("Germany"),
                    TestUtils.createCountryNetwork("United Kingdom")
                )
                coEvery { apiService.getCountries() } returns networks
                coEvery { countryDao.deleteAll() } just Runs
                coEvery { countryDao.insert(any<List<CountryEntity>>()) } just Runs

                "return list of Country" {
                    repository.updateCountries() shouldBe networks.toDomain()
                }

                "call apiService.getCountries()" {
                    coVerify { apiService.getCountries() }
                }

                "call countryDao.deleteAll()" {
                    coVerify { countryDao.deleteAll() }
                }

                "call countryDao.insert()" {
                    coVerify { countryDao.insert(any<List<CountryEntity>>()) }
                }

                "call database.countryDao()" {
                    coVerify(exactly = 2) { database.countryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountries() and api call failed" should {
                coEvery { apiService.getCountries() } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateCountries()
                }

                "call apiService.getCountries()" {
                    coVerify { apiService.getCountries() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountry() and api call succeeded" should {
                val network = TestUtils.createCountryNetwork("Poland")
                coEvery { apiService.getCountry(any()) } returns network
                coEvery { countryDao.insert(any<CountryEntity>()) } just Runs

                "return Country" {
                    repository.updateCountry("Poland") shouldBe network.toDomain()
                }

                "call apiService.updateCountry()" {
                    coVerify { apiService.getCountry(any()) }
                }

                "call countryDao.insert()" {
                    coVerify { countryDao.insert(any<CountryEntity>()) }
                }

                "call database.countryDao()" {
                    coVerify { database.countryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountry() and api call failed" should {
                coEvery { apiService.getCountry("Poland") } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateCountry("Poland")
                }

                "call apiService.getCountry()" {
                    coVerify { apiService.getCountry(any()) }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getCountryHistoryByNameObservable()" should {
                val entity = CountryHistoryAndTimelineRelation(
                    TestUtils.createCountryHistoryEntity("Poland"),
                    listOf(
                        TestUtils.createTimelineEntity(1, "Poland", TimelineType.CASE),
                        TestUtils.createTimelineEntity(2, "Poland", TimelineType.RECOVERY)
                    )
                )
                every { countryHistoryDao.getCountryHistoryAndTimeline(any<String>()) } returns MutableStateFlow(
                    entity
                )

                "emit CountryHistory" {
                    runBlockingTest {
                        repository.getCountryHistoryByNameObservable("Poland").test(this) {
                            assertValue { it == entity.toDomain() }
                            assertNotComplete()
                        }
                    }
                }

                "call countryHistoryDao.getCountryHistoryAndTimeline()" {
                    coVerify { countryHistoryDao.getCountryHistoryAndTimeline(any<String>()) }
                }

                "call database.countryDao()" {
                    coVerify { database.countryHistoryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getCountriesHistoryByNameObservable()" should {
                val entities = listOf(
                    CountryHistoryAndTimelineRelation(
                        TestUtils.createCountryHistoryEntity("Poland"),
                        listOf(
                            TestUtils.createTimelineEntity(1, "Poland", TimelineType.CASE),
                            TestUtils.createTimelineEntity(2, "Poland", TimelineType.RECOVERY)
                        )
                    ),
                    CountryHistoryAndTimelineRelation(
                        TestUtils.createCountryHistoryEntity("Germany"),
                        listOf(
                            TestUtils.createTimelineEntity(1, "Germany", TimelineType.CASE),
                            TestUtils.createTimelineEntity(2, "Germany", TimelineType.RECOVERY)
                        )
                    )
                )
                every { countryHistoryDao.getCountryHistoryAndTimeline(any<List<String>>()) } returns MutableStateFlow(
                    entities
                )

                "emit list of CountryHistory" {
                    runBlockingTest {
                        repository.getCountriesHistoryByNameObservable(setOf("Poland", "Germany"))
                            .test(this) {
                                assertValue { it == entities.toDomain() }
                                assertNotComplete()
                            }
                    }
                }

                "call countryHistoryDao.getCountryHistoryAndTimeline()" {
                    coVerify { countryHistoryDao.getCountryHistoryAndTimeline(any<List<String>>()) }
                }

                "call database.countryHistoryDao()" {
                    coVerify { database.countryHistoryDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountriesHistory() and api call succeeded" should {
                val networks = listOf(
                    TestUtils.createCountryHistoricalNetwork("Poland"),
                    TestUtils.createCountryHistoricalNetwork("Germany"),
                    TestUtils.createCountryHistoricalNetwork("United Kingdom")
                )
                coEvery { apiService.getHistorical() } returns networks
                coEvery { countryHistoryDao.deleteAll() } just Runs
                coEvery { countryHistoryDao.insert(any<List<CountryHistoryEntity>>()) } just Runs
                coEvery { timelineDao.deleteAll() } just Runs
                coEvery { timelineDao.insert(any<List<TimelineEntity>>()) } just Runs

                "return list of CountryHistory" {
                    repository.updateCountriesHistory() shouldBe networks.toDomain()
                }

                "call apiService.getHistorical()" {
                    coVerify { apiService.getHistorical() }
                }

                "call countryHistoryDao.deleteAll()" {
                    coVerify { countryHistoryDao.deleteAll() }
                }

                "call countryHistoryDao.insert()" {
                    coVerify { countryHistoryDao.insert(any<List<CountryHistoryEntity>>()) }
                }

                "call timelineDao.deleteAll()" {
                    coVerify { timelineDao.deleteAll() }
                }

                "call countryDao.insert()" {
                    coVerify { timelineDao.insert(any<List<TimelineEntity>>()) }
                }

                "call database.countryHistoryDao()" {
                    coVerify(exactly = 2) { database.countryHistoryDao() }
                }

                "call database.timelineDao()" {
                    coVerify(exactly = 2) { database.timelineDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountriesHistory() and api call failed" should {
                coEvery { apiService.getHistorical() } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateCountriesHistory()
                }

                "call apiService.getHistorical()" {
                    coVerify { apiService.getHistorical() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountryHistory() and api call succeeded" should {
                val network = TestUtils.createCountryHistoricalNetwork("Poland")
                coEvery { apiService.getHistorical(any()) } returns network
                coEvery { countryHistoryDao.insert(any<CountryHistoryEntity>()) } just Runs
                coEvery { timelineDao.insert(any<List<TimelineEntity>>()) } just Runs

                "return CountryHistory" {
                    repository.updateCountryHistory("Poland") shouldBe network.toDomain()
                }

                "call apiService.getHistorical()" {
                    coVerify { apiService.getHistorical(any()) }
                }

                "call countryHistoryDao.insert()" {
                    coVerify { countryHistoryDao.insert(any<CountryHistoryEntity>()) }
                }

                "call timelineDao.insert()" {
                    coVerify { timelineDao.insert(any<List<TimelineEntity>>()) }
                }

                "call database.countryHistoryDao()" {
                    coVerify { database.countryHistoryDao() }
                }

                "call database.timelineDao()" {
                    coVerify { database.timelineDao() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called updateCountryHistory() and api call failed" should {
                coEvery { apiService.getHistorical("Poland") } throws TestUtils.createHttpException(404)

                shouldThrow<HttpException> {
                    repository.updateCountryHistory("Poland")
                }

                "call apiService.getHistorical()" {
                    coVerify { apiService.getHistorical("Poland") }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getFavoriteCountriesNamesObservable()" should {
                val countries = setOf("Poland", "Germany")
                every { keyValueStore.favoriteCountriesObservable } returns MutableStateFlow(
                    countries
                )

                "emit set of country names" {
                    runBlockingTest {
                        repository.getFavoriteCountriesNamesObservable().test(this) {
                            assertValue { it == countries }
                            assertNotComplete()
                        }
                    }
                }

                "get keyValueStore.favoriteCountriesObservable" {
                    coVerify { keyValueStore.favoriteCountriesObservable }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called addCountryToFavorites('Poland') when only 'Germany' is in favorites" should {
                val countries = setOf("Germany")
                every { keyValueStore.favoriteCountries } returns countries

                repository.addCountryToFavorites("Poland")

                "call get keyValueStore.favoriteCountries" {
                    coVerify { keyValueStore.favoriteCountries }
                }

                "call set keyValueStore.favoriteCountries with added 'Poland'" {
                    coVerify { keyValueStore.favoriteCountries = countries.plus("Poland") }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called addCountryToFavorites('Poland') when only 'Poland' is in favorites" should {
                val countries = setOf("Poland")
                every { keyValueStore.favoriteCountries } returns countries

                repository.addCountryToFavorites("Poland")

                "call get keyValueStore.favoriteCountries" {
                    coVerify { keyValueStore.favoriteCountries }
                }

                "call set keyValueStore.favoriteCountries with unchanged set" {
                    coVerify { keyValueStore.favoriteCountries = countries }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called removeCountryFromFavorites('Poland') when only 'Germany' is in favorites" should {
                val countries = setOf("Germany")
                every { keyValueStore.favoriteCountries } returns countries

                repository.removeCountryFromFavorites("Poland")

                "call get keyValueStore.favoriteCountries" {
                    coVerify { keyValueStore.favoriteCountries }
                }

                "call set keyValueStore.favoriteCountries with unchanged set" {
                    coVerify { keyValueStore.favoriteCountries = countries }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called removeCountryFromFavorites('Poland') when only 'Poland' is in favorites" should {
                val countries = setOf("Poland")
                every { keyValueStore.favoriteCountries } returns countries

                repository.removeCountryFromFavorites("Poland")

                "call get keyValueStore.favoriteCountries" {
                    coVerify { keyValueStore.favoriteCountries }
                }

                "call set keyValueStore.favoriteCountries with empty set" {
                    coVerify { keyValueStore.favoriteCountries = emptySet() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called getComparisonCountriesNamesObservable()" should {
                val countries = setOf("Poland", "Germany")
                every { keyValueStore.comparisonCountriesObservable } returns MutableStateFlow(
                    countries
                )

                "emit set of country names" {
                    runBlockingTest {
                        repository.getComparisonCountriesNamesObservable().test(this) {
                            assertValue { it == countries }
                            assertNotComplete()
                        }
                    }
                }

                "get keyValueStore.comparisonCountriesObservable" {
                    coVerify { keyValueStore.comparisonCountriesObservable }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called addCountryToComparison('Poland') when only 'Germany' is in comparison" should {
                val countries = setOf("Germany")
                every { keyValueStore.comparisonCountries } returns countries

                repository.addCountryToComparison("Poland")

                "call get keyValueStore.comparisonCountries" {
                    coVerify { keyValueStore.comparisonCountries }
                }

                "call set keyValueStore.comparisonCountries with added 'Poland'" {
                    coVerify { keyValueStore.comparisonCountries = countries.plus("Poland") }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called addCountryToComparison('Poland') when only 'Poland' is in comparison" should {
                val countries = setOf("Poland")
                every { keyValueStore.comparisonCountries } returns countries

                repository.addCountryToComparison("Poland")

                "call get keyValueStore.comparisonCountries" {
                    coVerify { keyValueStore.comparisonCountries }
                }

                "call set keyValueStore.comparisonCountries with unchanged set" {
                    coVerify { keyValueStore.comparisonCountries = countries }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called removeCountryFromComparison('Poland') when only 'Germany' is in comparison" should {
                val countries = setOf("Germany")
                every { keyValueStore.comparisonCountries } returns countries

                repository.removeCountryFromComparison("Poland")

                "call get keyValueStore.comparisonCountries" {
                    coVerify { keyValueStore.comparisonCountries }
                }

                "call set keyValueStore.comparisonCountries with unchanged set" {
                    coVerify { keyValueStore.comparisonCountries = countries }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called removeCountryFromComparison('Poland') when only 'Poland' is in comparison" should {
                val countries = setOf("Poland")
                every { keyValueStore.comparisonCountries } returns countries

                repository.removeCountryFromComparison("Poland")

                "call get keyValueStore.comparisonCountries" {
                    coVerify { keyValueStore.comparisonCountries }
                }

                "call set keyValueStore.comparisonCountries with empty set" {
                    coVerify { keyValueStore.comparisonCountries = emptySet() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }

            "called resetComparisonCountries()" should {
                repository.resetComparisonCountries()

                "call set keyValueStore.comparisonCountries with empty set" {
                    coVerify { keyValueStore.comparisonCountries = emptySet() }
                }

                confirmVerified(
                    apiService,
                    database,
                    keyValueStore,
                    countryDao,
                    countryHistoryDao,
                    timelineDao
                )
                clearAllMocks()
            }
        }
    }
}