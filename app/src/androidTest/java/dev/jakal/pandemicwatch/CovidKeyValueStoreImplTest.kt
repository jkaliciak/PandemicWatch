package dev.jakal.pandemicwatch

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.moshi.Moshi
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.CovidKeyValueStoreImpl
import dev.jakal.pandemicwatch.utils.TestUtils
import dev.olog.flow.test.observer.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CovidKeyValueStoreImplTest {

    private lateinit var covidKeyValueStore: CovidKeyValueStoreImpl

    @Before
    fun before() {
        covidKeyValueStore = CovidKeyValueStoreImpl(
            InstrumentationRegistry.getInstrumentation().context,
            Moshi.Builder().build()
        )
    }

    @After
    fun after() {
        covidKeyValueStore.globalStats = null
        covidKeyValueStore.globalHistory = null
        covidKeyValueStore.favoriteCountries = mutableSetOf()
        covidKeyValueStore.comparisonCountries = mutableSetOf()
    }

    @Test
    fun globalStatsShouldReturnEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalStats = TestUtils.createGlobalStats()
        covidKeyValueStore.globalStats = globalStats

        // when
        val result = covidKeyValueStore.globalStats

        // than
        result shouldBe globalStats
    }

    @Test
    fun globalStatsShouldReturnNullWhenEntityWasInsertedAndDeleted() = runBlockingTest {
        // given
        val globalStats = TestUtils.createGlobalStats()
        covidKeyValueStore.globalStats = globalStats
        covidKeyValueStore.globalStats = null

        // when
        val result = covidKeyValueStore.globalStats

        // than
        result shouldBe null
    }

    @Test
    fun globalStatsShouldReturnNullWhenEmpty() = runBlockingTest {
        // given

        // when
        val result = covidKeyValueStore.globalStats

        // than
        result shouldBe null
    }

    @Test
    fun globalStatsObservableShouldEmitEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalStats = TestUtils.createGlobalStats()

        // when
        covidKeyValueStore.globalStatsObservable.test(this) {
            // than
            assertValue { it == globalStats }
            assertNotComplete()
        }
        covidKeyValueStore.globalStats = globalStats
    }

    @Test
    fun globalStatsObservableShouldEmitEntityAndNothingWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalStats = TestUtils.createGlobalStats()

        // when
        covidKeyValueStore.globalStatsObservable.test(this) {
            // than
            assertValue { it == globalStats }
            assertNotComplete()
        }
        covidKeyValueStore.globalStats = globalStats
        covidKeyValueStore.globalStats = null
    }

    @Test
    fun globalStatsObservableShouldEmitNothingWhenEmpty() = runBlockingTest {
        // given

        // when
        covidKeyValueStore.globalStatsObservable.test(this) {
            // than
            assertNoValues()
            assertNotComplete()
        }
    }

    @Test
    fun globalHistoryShouldReturnEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalHistory = TestUtils.createGlobalHistory()
        covidKeyValueStore.globalHistory = globalHistory

        // when
        val result = covidKeyValueStore.globalHistory

        // than
        result shouldBe globalHistory
    }

    @Test
    fun globalHistoryShouldReturnNullWhenEntityWasInsertedAndDeleted() = runBlockingTest {
        // given
        val globalHistory = TestUtils.createGlobalHistory()
        covidKeyValueStore.globalHistory = globalHistory
        covidKeyValueStore.globalHistory = null

        // when
        val result = covidKeyValueStore.globalHistory

        // than
        result shouldBe null
    }

    @Test
    fun globalHistoryShouldReturnNullWhenEmpty() = runBlockingTest {
        // given

        // when
        val result = covidKeyValueStore.globalHistory

        // than
        result shouldBe null
    }

    @Test
    fun globalHistoryObservableShouldEmitEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalHistory = TestUtils.createGlobalHistory()

        // when
        covidKeyValueStore.globalHistoryObservable.test(this) {
            // than
            assertValue { it == globalHistory }
            assertNotComplete()
        }
        covidKeyValueStore.globalHistory = globalHistory
    }

    @Test
    fun globalHistoryObservableShouldEmitEntityAndNothingWhenEntityWasInserted() = runBlockingTest {
        // given
        val globalHistory = TestUtils.createGlobalHistory()

        // when
        covidKeyValueStore.globalHistoryObservable.test(this) {
            // than
            assertValue { it == globalHistory }
            assertNotComplete()
        }
        covidKeyValueStore.globalHistory = globalHistory
        covidKeyValueStore.globalHistory = null
    }

    @Test
    fun globalHistoryObservableShouldEmitNothingWhenEmpty() = runBlockingTest {
        // given

        // when
        covidKeyValueStore.globalHistoryObservable.test(this) {
            // than
            assertNoValues()
            assertNotComplete()
        }
    }

    @Test
    fun favoriteCountriesShouldReturnEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")
        covidKeyValueStore.favoriteCountries = countries

        // when
        val result = covidKeyValueStore.favoriteCountries

        // than
        result shouldBe countries
    }

    @Test
    fun favoriteCountriesShouldReturnEmptySetWhenEntityWasInsertedAndDeleted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")
        covidKeyValueStore.favoriteCountries = countries
        covidKeyValueStore.favoriteCountries = emptySet()

        // when
        val result = covidKeyValueStore.favoriteCountries

        // than
        result shouldBe emptySet()
    }

    @Test
    fun favoriteCountriesShouldReturnEmptySetWhenEmpty() = runBlockingTest {
        // given

        // when
        val result = covidKeyValueStore.favoriteCountries

        // than
        result shouldBe emptySet()
    }

    @Test
    fun favoriteCountriesObservableShouldEmitEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")

        // when
        covidKeyValueStore.favoriteCountriesObservable.test(this) {
            // than
            assertValues(
                emptySet(),
                countries
            )
            assertNotComplete()
        }
        covidKeyValueStore.favoriteCountries = countries
    }

    @Test
    fun favoriteCountriesObservableShouldEmitEntityAndNothingWhenEntityWasInserted() =
        runBlockingTest {
            // given
            val countries = setOf("Poland", "Germany")

            // when
            covidKeyValueStore.favoriteCountriesObservable.test(this) {
                // than
                assertValues(
                    emptySet(),
                    countries,
                    emptySet()
                )
                assertNotComplete()
            }
            covidKeyValueStore.favoriteCountries = countries
            covidKeyValueStore.favoriteCountries = emptySet()
        }

    @Test
    fun favoriteCountriesObservableShouldEmitNothingWhenEmpty() = runBlockingTest {
        // given

        // when
        covidKeyValueStore.favoriteCountriesObservable.test(this) {
            // than
            assertValue { it.isEmpty() }
            assertNotComplete()
        }
    }

    @Test
    fun comparisonCountriesShouldReturnEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")
        covidKeyValueStore.comparisonCountries = countries

        // when
        val result = covidKeyValueStore.comparisonCountries

        // than
        result shouldBe countries
    }

    @Test
    fun comparisonCountriesShouldReturnEmptySetWhenEntityWasInsertedAndDeleted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")
        covidKeyValueStore.comparisonCountries = countries
        covidKeyValueStore.comparisonCountries = emptySet()

        // when
        val result = covidKeyValueStore.comparisonCountries

        // than
        result shouldBe emptySet()
    }

    @Test
    fun comparisonCountriesShouldReturnEmptySetWhenEmpty() = runBlockingTest {
        // given

        // when
        val result = covidKeyValueStore.comparisonCountries

        // than
        result shouldBe emptySet()
    }

    @Test
    fun comparisonCountriesObservableShouldEmitEntityWhenEntityWasInserted() = runBlockingTest {
        // given
        val countries = setOf("Poland", "Germany")

        // when
        covidKeyValueStore.comparisonCountriesObservable.test(this) {
            // than
            assertValues(
                emptySet(),
                countries
            )
            assertNotComplete()
        }
        covidKeyValueStore.comparisonCountries = countries
    }

    @Test
    fun comparisonCountriesObservableShouldEmitEntityAndNothingWhenEntityWasInserted() =
        runBlockingTest {
            // given
            val countries = setOf("Poland", "Germany")

            // when
            covidKeyValueStore.comparisonCountriesObservable.test(this) {
                // than
                assertValues(
                    emptySet(),
                    countries,
                    emptySet()
                )
                assertNotComplete()
            }
            covidKeyValueStore.comparisonCountries = countries
            covidKeyValueStore.comparisonCountries = emptySet()
        }

    @Test
    fun comparisonCountriesObservableShouldEmitNothingWhenEmpty() = runBlockingTest {
        // given

        // when
        covidKeyValueStore.comparisonCountriesObservable.test(this) {
            // than
            assertValue { it.isEmpty() }
            assertNotComplete()
        }
    }
}