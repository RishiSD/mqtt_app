package com.example.testmc.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testmc.models.MqttConnection
import com.example.testmc.models.MqttTopic

@Database(
    entities = [MqttConnection::class, MqttTopic::class],
    version = 1,
    exportSchema = false)
public abstract class MqttConnectionDatabase: RoomDatabase() {

    abstract fun getMqttConnectionDao(): MqttConnectionDao
    abstract fun getMqttTopicDao(): MqttTopicDao // XXX: New

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MqttConnectionDatabase? = null

        fun getDatabase(context: Context): MqttConnectionDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MqttConnectionDatabase::class.java,
                    "notes_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}