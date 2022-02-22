package com.example.testmc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmc.data.MqttConnectionDatabase
import com.example.testmc.models.MqttTopic
import com.example.testmc.repositories.IMqttConnectionRepository
import com.example.testmc.repositories.IMqttTopicRepository
import com.example.testmc.repositories.MqttTopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val repository: IMqttTopicRepository
) : ViewModel(), ITopicViewModel {

    val allTopics: LiveData<List<MqttTopic>> = repository.allTopics

    override fun insertMqttTopic(mqttTopic: MqttTopic) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(mqttTopic)
        }

    override fun updateMqttTopic(mqttTopic: MqttTopic) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(mqttTopic)
        }

    override fun deleteMqttTopic(mqttTopic: MqttTopic) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(mqttTopic)
        }
}

interface ITopicViewModel {
    fun insertMqttTopic(mqttTopic: MqttTopic): Job
    fun updateMqttTopic(mqttTopic: MqttTopic): Job
    fun deleteMqttTopic(mqttTopic: MqttTopic): Job
}