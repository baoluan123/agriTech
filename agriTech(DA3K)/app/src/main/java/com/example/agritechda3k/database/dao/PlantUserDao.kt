package com.example.agritechda3k.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agritechda3k.model.PlantUser
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantUserDao {
    @Query("SELECT * FROM plant_user")
    fun getAllMyPlants(): Flow<List<PlantUser>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myPlants: List<PlantUser>)
    @Query("DELETE FROM plant_user")
    suspend fun deleteAll()
    @Query("SELECT * FROM plant_user WHERE id = :id")
    fun getMyPlantById(id: Long): Flow<PlantUser?>
}