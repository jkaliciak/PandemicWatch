package dev.jakal.pandemicwatch

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryHistoryDao
import dev.jakal.pandemicwatch.infrastructure.database.dao.TimelineDao
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoryAndTimelineRelation
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineType
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryHistoryDaoAndTimelineDaoTest {

    private lateinit var database: CovidDatabase
    private lateinit var countryHistoryDao: CountryHistoryDao
    private lateinit var timelineDao: TimelineDao

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            CovidDatabase::class.java
        ).build()
        countryHistoryDao = database.countryHistoryDao()
        timelineDao = database.timelineDao()
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun getAllCountryHistoryAndTimelineShouldReturnAllEntityWhenEntityWasInserted() = runBlocking {
        // given
        val countryHistory = TestUtils.createCountryHistory("Poland")
        val timelines = listOf(
            TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
            TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
        )
        countryHistoryDao.insert(countryHistory)
        timelineDao.insert(timelines)

        // when
        val result = countryHistoryDao.getAllCountryHistoryAndTimeline()
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 1
        result.first().historyEntity shouldBe countryHistory
        result.first().timelineEntities shouldContainExactlyInAnyOrder timelines
    }

    @Test
    fun getAllCountryHistoryAndTimelineShouldReturnNothingWhenEntityWasInsertedAndDeleted() =
        runBlocking {
            // given
            val countryHistory = TestUtils.createCountryHistory("Poland")
            val timelines = listOf(
                TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
                TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
            )
            countryHistoryDao.insert(countryHistory)
            timelineDao.insert(timelines)
            countryHistoryDao.deleteAll()
            timelineDao.deleteAll()

            // when
            val result = countryHistoryDao.getAllCountryHistoryAndTimeline()
                .take(1)
                .toList()
                .flatten()

            // than
            result shouldHaveSize 0
        }

    @Test
    fun getAllCountryHistoryAndTimelineShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryHistoryDao.getAllCountryHistoryAndTimeline()
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }

    @Test
    fun getCountryHistoryAndTimelineShouldReturnRequestedEntitiesWhenEntitiesWereInserted() =
        runBlocking {
            // given
            val poland = TestUtils.createCountryHistory("Poland")
            val germany = TestUtils.createCountryHistory("Germany")
            val polandTimelines = listOf(
                TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
                TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
            )
            val germanyTimelines = listOf(
                TestUtils.createTimeline(3, "Germany", TimelineType.CASE),
                TestUtils.createTimeline(4, "Germany", TimelineType.RECOVERY)
            )
            countryHistoryDao.insert(poland, germany)
            timelineDao.insert(polandTimelines.plus(germanyTimelines))

            // when
            val result = countryHistoryDao.getCountryHistoryAndTimeline("Poland")
                .take(1)
                .toList()

            // than
            result shouldHaveSize 1
            result.first().historyEntity shouldBe poland
            result.first().timelineEntities shouldContainExactlyInAnyOrder polandTimelines
        }

    @Test
    fun getCountryHistoryAndTimelineShouldReturnNothingWhenEntitiesWereInsertedDeleted() =
        runBlocking {
            // given
            val poland = TestUtils.createCountryHistory("Poland")
            val polandTimelines = listOf(
                TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
                TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
            )
            countryHistoryDao.insert(poland)
            timelineDao.insert(polandTimelines)
            countryHistoryDao.deleteAll()
            timelineDao.deleteAll()

            // when
            val result = countryHistoryDao.getCountryHistoryAndTimeline("Poland")
                .take(1)
                .toList()

            // than
            result shouldContainExactly listOf(null)
        }

    @Test
    fun getCountryHistoryAndTimelineShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryHistoryDao.getCountryHistoryAndTimeline("Poland")
            .take(1)
            .toList()

        // than
        result shouldContainExactly listOf(null)
    }

    @Test
    fun getCountryHistoryAndTimelineListShouldReturnRequestedEntitiesWhenEntitiesWereInserted() =
        runBlocking {
            // given
            val poland = TestUtils.createCountryHistory("Poland")
            val germany = TestUtils.createCountryHistory("Germany")
            val uk = TestUtils.createCountryHistory("United Kingdom")
            val polandTimelines = listOf(
                TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
                TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
            )
            val germanyTimelines = listOf(
                TestUtils.createTimeline(3, "Germany", TimelineType.CASE),
                TestUtils.createTimeline(4, "Germany", TimelineType.RECOVERY)
            )
            val ukTimelines = listOf(
                TestUtils.createTimeline(5, "United Kingdom", TimelineType.CASE),
                TestUtils.createTimeline(6, "United Kingdom", TimelineType.RECOVERY)
            )
            countryHistoryDao.insert(poland, germany, uk)
            timelineDao.insert(polandTimelines.plus(germanyTimelines).plus(ukTimelines))

            // when
            val result = countryHistoryDao.getCountryHistoryAndTimeline(listOf("Poland", "Germany"))
                .take(1)
                .toList()
                .flatten()

            // than
            result shouldHaveSize 2
            result shouldContainExactlyInAnyOrder listOf(
                CountryHistoryAndTimelineRelation(
                    poland,
                    polandTimelines
                ),
                CountryHistoryAndTimelineRelation(
                    germany,
                    germanyTimelines
                )
            )
        }

    @Test
    fun getCountryHistoryAndTimelineListShouldReturnNothingWhenEntitiesWereInsertedDeleted() =
        runBlocking {
            // given
            val poland = TestUtils.createCountryHistory("Poland")
            val germany = TestUtils.createCountryHistory("Germany")
            val uk = TestUtils.createCountryHistory("United Kingdom")
            val polandTimelines = listOf(
                TestUtils.createTimeline(1, "Poland", TimelineType.CASE),
                TestUtils.createTimeline(2, "Poland", TimelineType.RECOVERY)
            )
            val germanyTimelines = listOf(
                TestUtils.createTimeline(3, "Germany", TimelineType.CASE),
                TestUtils.createTimeline(4, "Germany", TimelineType.RECOVERY)
            )
            val ukTimelines = listOf(
                TestUtils.createTimeline(5, "United Kingdom", TimelineType.CASE),
                TestUtils.createTimeline(6, "United Kingdom", TimelineType.RECOVERY)
            )
            countryHistoryDao.insert(poland, germany, uk)
            timelineDao.insert(polandTimelines.plus(germanyTimelines).plus(ukTimelines))
            countryHistoryDao.deleteAll()
            timelineDao.deleteAll()

            // when
            val result = countryHistoryDao.getCountryHistoryAndTimeline(listOf("Poland", "Germany"))
                .take(1)
                .toList()
                .flatten()

            // than
            result shouldHaveSize 0
        }

    @Test
    fun getCountryHistoryAndTimelineListShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryHistoryDao.getCountryHistoryAndTimeline(listOf("Poland", "Germany"))
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }
}