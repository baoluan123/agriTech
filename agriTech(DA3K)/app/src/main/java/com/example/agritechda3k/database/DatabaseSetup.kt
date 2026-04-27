package com.example.agritechda3k.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.agritechda3k.database.dao.AuthDao
import com.example.agritechda3k.database.dao.NotificationDao
import com.example.agritechda3k.database.dao.PlantDao
import com.example.agritechda3k.database.dao.PlantUserDao
import com.example.agritechda3k.model.Auth
import com.example.agritechda3k.model.Notification
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.model.PlantUser


@Database(entities = [Plant::class, PlantUser::class, Auth::class, Notification::class], version = 7, exportSchema = false)
abstract class DatabaseSetup: RoomDatabase() {
    //***
    abstract fun plantDao(): PlantDao
    abstract fun planUserDao(): PlantUserDao
    abstract fun authDao(): AuthDao
    abstract fun notificationDao(): NotificationDao

    companion object{
        @Volatile
        private var INSTANCE: DatabaseSetup?=null
        ///****
        fun getDatabase(context: Context): DatabaseSetup{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseSetup::class.java,
                    "agritech_db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}