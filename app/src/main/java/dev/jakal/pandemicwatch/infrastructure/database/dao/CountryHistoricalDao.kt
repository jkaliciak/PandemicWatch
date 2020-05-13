package dev.jakal.pandemicwatch.infrastructure.database.dao

import androidx.room.*
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoricalAndTimelineRelation
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoricalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryHistoricalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg countriesHistorical: CountryHistoricalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(countriesHistorical: List<CountryHistoricalEntity>)

    @Transaction
    @Query("SELECT * FROM country_historical")
    fun getAllCountryHistoricalAndTimeline(): Flow<List<CountryHistoricalAndTimelineRelation>>

    @Transaction
    @Query("SELECT * FROM country_historical WHERE country_historical.country LIKE :countryName")
    fun getCountryHistoricalAndTimeline(countryName: String): Flow<CountryHistoricalAndTimelineRelation>

    @Query("DELETE FROM country_historical")
    suspend fun deleteAll()
}