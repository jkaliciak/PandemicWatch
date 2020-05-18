package dev.jakal.pandemicwatch.infrastructure.database.dao

import androidx.room.*
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoryAndTimelineRelation
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg countriesHistory: CountryHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(countriesHistory: List<CountryHistoryEntity>)

    @Transaction
    @Query("SELECT * FROM country_history")
    fun getAllCountryHistoryAndTimeline(): Flow<List<CountryHistoryAndTimelineRelation>>

    @Transaction
    @Query("SELECT * FROM country_history WHERE country_history.country LIKE :countryName")
    fun getCountryHistoryAndTimeline(countryName: String): Flow<CountryHistoryAndTimelineRelation>

    @Transaction
    @Query("SELECT * FROM country_history WHERE country_history.country IN (:countryNames)")
    fun getCountryHistoryAndTimeline(countryNames: List<String>): Flow<List<CountryHistoryAndTimelineRelation>>

    @Query("DELETE FROM country_history")
    suspend fun deleteAll()
}