package com.example.testmc.repositories

import androidx.lifecycle.MutableLiveData
import com.example.testmc.models.MqttConnection

class FakeMqttConnRepo: IMqttConnectionRepository {

    val allConnList = mutableListOf<MqttConnection>()
    override val allConnections = MutableLiveData<List<MqttConnection>>(allConnList)
    private var id = 0

    init {
        allConnList.add(MqttConnection("testConn0",
            "testClient", "test.mqtt.broker", 1883))
        allConnList.add(MqttConnection("testConn10",
            "testClient", "test.mqtt.broker", 1883))
    }

    override suspend fun insert(mqttConnection: MqttConnection) {
        mqttConnection.id = id++
        allConnList.add(mqttConnection)
    }

    override suspend fun update(mqttConnection: MqttConnection) {
        val conn = allConnList.first { it.id == mqttConnection.id }
        mqttConnection.id = conn.id
        allConnList.remove(conn)
        allConnList.add(mqttConnection)
    }

    override suspend fun delete(mqttConnection: MqttConnection) {
        allConnList.remove(mqttConnection)
    }

    fun clear() {
        allConnList.clear()
    }
}