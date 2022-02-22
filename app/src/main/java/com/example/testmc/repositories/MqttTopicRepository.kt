package com.example.testmc.repositories

import androidx.lifecycle.LiveData
import com.example.testmc.data.MqttConnectionDao
import com.example.testmc.data.MqttTopicDao
import com.example.testmc.models.MqttConnection
import com.example.testmc.models.MqttTopic
import javax.inject.Inject

class MqttTopicRepository @Inject constructor(
    private val mqttTopicDao: MqttTopicDao): IMqttTopicRepository {

    override var allTopics: LiveData<List<MqttTopic>> =  mqttTopicDao.getAllTopics()

    override suspend fun insert(mqttTopic: MqttTopic) {
        mqttTopicDao.insert(mqttTopic)
    }

    override suspend fun update(mqttTopic: MqttTopic) {
        mqttTopicDao.update(mqttTopic)
    }

    override suspend fun delete(mqttTopic: MqttTopic) {
        mqttTopicDao.delete(mqttTopic)
    }
}

interface IMqttTopicRepository {
    val allTopics: LiveData<List<MqttTopic>>
    suspend fun insert(mqttTopic: MqttTopic)
    suspend fun update(mqttTopic: MqttTopic)
    suspend fun delete(mqttTopic: MqttTopic)
}