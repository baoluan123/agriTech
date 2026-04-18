package com.example.agritechda3k.model




import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "name_plant")
    val namePlant: String,

    @ColumnInfo(name = "ideal_humidity")
    val idealHumidity: Float? = null,

    @ColumnInfo(name = "water_frequency")
    val waterFrequency: Int? = null,

    @ColumnInfo(name = "fertilizer_info")
    val fertilizerInfo: String? = null,

    @ColumnInfo(name = "description_plant")
    val descriptionPlant: String? = null,
    @ColumnInfo(name = "thumbnail_url") // Thêm cột này để lưu link ảnh chính
    val thumbnailUrl: String? = null


)