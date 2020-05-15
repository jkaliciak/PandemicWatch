package dev.jakal.pandemicwatch.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg countries: CountryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(countries: List<CountryEntity>)

    @Query("SELECT * FROM country WHERE country.country LIKE :countryName")
    fun getByCountryName(countryName: String): Flow<CountryEntity>

    @Query("SELECT * FROM country WHERE country.country IN (:countryNames)")
    fun getAllByCountryName(countryNames: List<String>): Flow<List<CountryEntity>>

    @Query("SELECT * FROM country")
    fun getAll(): Flow<List<CountryEntity>>

    @Query("DELETE FROM country")
    suspend fun deleteAll()
}