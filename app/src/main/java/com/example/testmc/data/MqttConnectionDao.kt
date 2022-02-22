package com.example.testmc.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testmc.models.MqttConnection
import com.example.testmc.models.MqttTopic

@Dao
interface MqttConnectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mqttConnection: MqttConnection)

    @Update
    suspend fun update(mqttConnection: MqttConnection)

    @Delete
    suspend fun delete(mqttConnection: MqttConnection)

    @Query("select * from mqtt_connection order by id ASC")
    fun getAllConnections(): LiveData<List<MqttConnection>>
}