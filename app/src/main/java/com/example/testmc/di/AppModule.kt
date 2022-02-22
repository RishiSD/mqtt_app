package com.example.testmc.di

import android.content.Context
import androidx.room.Room
import com.example.testmc.data.MqttConnectionDao
import com.example.testmc.data.MqttConnectionDatabase
import com.example.testmc.data.MqttTopicDao
import com.example.testmc.repositories.IMqttConnectionRepository
import com.example.testmc.repositories.IMqttTopicRepository
import com.example.testmc.repositories.MqttConnectionRepository
import com.example.testmc.repositories.MqttTopicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideConnDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder( context.applicationContext,
        MqttConnectionDatabase::class.java, "notes_database")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesConnRepository(
        dao: MqttConnectionDao
    ) = MqttConnectionRepository(dao) as IMqttConnectionRepository

    @Singleton
    @Provides
    fun provideConnDao(
        database: MqttConnectionDatabase
    ) = database.getMqttConnectionDao() as MqttConnectionDao

    @Singleton
    @Provides
    fun providesTopicRepository(
        dao: MqttTopicDao
    ) = MqttTopicRepository(dao) as IMqttTopicRepository

    @Singleton
    @Provides
    fun provideTopicDao(
        database: MqttConnectionDatabase
    ) = database.getMqttTopicDao() as MqttTopicDao
}