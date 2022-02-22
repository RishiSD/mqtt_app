package com.example.testmc.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testmc.data.MqttConnectionDao
import com.example.testmc.models.MqttConnection
import javax.inject.Inject

class MqttConnectionRepository @Inject constructor(
    private val mqttConnectionDao: MqttConnectionDao): IMqttConnectionRepository {

    override val allConnections: LiveData<List<MqttConnection>> =  mqttConnectionDao.getAllConnections()

    override suspend fun insert(MqttConnection: MqttConnection) {
        mqttConnectionDao.insert(MqttConnection)
    }

    override suspend fun update(MqttConnection: MqttConnection) {
        mqttConnectionDao.update(MqttConnection)
    }

    override suspend fun delete(MqttConnection: MqttConnection) {
        mqttConnectionDao.delete(MqttConnection)
    }
}

interface IMqttConnectionRepository {
    val allConnections: LiveData<List<MqttConnection>>
    suspend fun insert(MqttConnection: MqttConnection)
    suspend fun update(MqttConnection: MqttConnection)
    suspend fun delete(MqttConnection: MqttConnection)
}
