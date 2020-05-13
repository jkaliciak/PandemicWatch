package dev.jakal.pandemicwatch.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineEntity

@Dao
interface TimelineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg timelines: TimelineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timelines: List<TimelineEntity>)

    @Query("DELETE FROM timeline")
    suspend fun deleteAll()
}