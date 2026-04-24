package com.example.agritechda3k.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agritechda3k.api.dto.PlantDetailsDTO
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.model.PlantUser
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants")
    fun getAllPlant(): Flow<List<Plant>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlant(plant: List<Plant>)
    @Query("SELECT * FROM plants where id = :id")
    suspend fun getPlantById(id:Long): Plant?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlantDetail(plant: Plant)
    @Query("SELECT id FROM auths")
    suspend fun getAuth(): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlantUser(plantUser: PlantUser)


}