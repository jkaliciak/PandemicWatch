package dev.jakal.pandemicwatch

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.jakal.pandemicwatch.infrastructure.database.CovidDatabase
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryDao
import dev.jakal.pandemicwatch.utils.TestUtils
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryDaoTest {

    private lateinit var database: CovidDatabase
    private lateinit var countryDao: CountryDao

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            CovidDatabase::class.java
        ).build()
        countryDao = database.countryDao()
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun getByCountryNameShouldReturnEntityWhenEntityWasInserted() = runBlocking {
        // given
        val country = TestUtils.createCountryEntity("Poland")
        countryDao.insert(listOf(country))

        // when
        val result = countryDao.getByCountryName("Poland")
            .take(1)
            .toList()

        // than
        result shouldContain country
    }

    @Test
    fun getByCountryNameShouldReturnNothingWhenEntityWasInsertedAndDeleted() = runBlocking {
        // given
        val country = TestUtils.createCountryEntity("Poland")
        countryDao.insert(listOf(country))
        countryDao.deleteAll()

        // when
        val result = countryDao.getByCountryName("Poland")
            .take(1)
            .toList()

        // than
        result shouldContainExactly listOf(null)
    }

    @Test
    fun getByCountryNameShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryDao.getByCountryName("Poland")
            .take(1)
            .toList()

        // than
        result shouldContainExactly listOf(null)
    }

    @Test
    fun getAllByCountryNameShouldReturnRequestedEntitiesWhenEntitiesWereInserted() = runBlocking {
        // given
        val poland = TestUtils.createCountryEntity("Poland")
        val germany = TestUtils.createCountryEntity("Germany")
        val uk = TestUtils.createCountryEntity("United Kingdom")
        countryDao.insert(listOf(poland, germany, uk))

        // when
        val result = countryDao.getAllByCountryName(listOf("Poland", "Germany"))
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldContainExactlyInAnyOrder listOf(poland, germany)
    }

    @Test
    fun getAllByCountryNameShouldReturnNothingWhenEntitiesWereInsertedAndDeleted() = runBlocking {
        // given
        val poland = TestUtils.createCountryEntity("Poland")
        val germany = TestUtils.createCountryEntity("Germany")
        val uk = TestUtils.createCountryEntity("United Kingdom")
        countryDao.insert(listOf(poland, germany, uk))
        countryDao.deleteAll()

        // when
        val result = countryDao.getAllByCountryName(listOf("Poland", "Germany", "United Kingdom"))
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }

    @Test
    fun getAllByCountryNameShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryDao.getAllByCountryName(listOf("Poland", "Germany", "United Kingdom"))
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }

    @Test
    fun getAllShouldReturnEntityWhenEntityWasInserted() = runBlocking {
        // given
        val country = TestUtils.createCountryEntity("Poland")
        countryDao.insert(country)

        // when
        val result = countryDao.getAll()
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldContain country
    }

    @Test
    fun getAllShouldReturnNothingWhenEntityWasInsertedAndDeleted() = runBlocking {
        // given
        val country = TestUtils.createCountryEntity("Poland")
        countryDao.insert(country)
        countryDao.deleteAll()

        // when
        val result = countryDao.getAll()
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }

    @Test
    fun getAllShouldReturnNothingWhenEmpty() = runBlocking {
        // given

        // when
        val result = countryDao.getAll()
            .take(1)
            .toList()
            .flatten()

        // than
        result shouldHaveSize 0
    }
}