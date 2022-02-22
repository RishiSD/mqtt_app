package com.example.testmc.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testmc.models.MqttTopic

@Dao
interface MqttTopicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mqttTopic: MqttTopic)

    @Update
    suspend fun update(mqttTopic: MqttTopic)

    @Query("select * from mqtt_topic where connId = :connID order by id ASC")
    fun getAllTopicsByConnId(connID: Int): LiveData<List<MqttTopic>>

    @Query("select * from mqtt_topic order by id ASC")
    fun getAllTopics(): LiveData<List<MqttTopic>>

    @Delete
    suspend fun delete(mqttTopic: MqttTopic)
}