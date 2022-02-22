package com.example.testmc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmc.data.MqttConnectionDatabase
import com.example.testmc.models.MqttConnection
import com.example.testmc.repositories.IMqttConnectionRepository
import com.example.testmc.repositories.MqttConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttConnectionViewModel @Inject constructor(
        private val repository: IMqttConnectionRepository
    ) : ViewModel(), IMqttConnectionViewModel
{
    val allConnections: LiveData<List<MqttConnection>> = repository.allConnections

    override fun insertMqttConnection(MqttConnection: MqttConnection) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(MqttConnection)
        }

    override fun updateMqttConnection(MqttConnection: MqttConnection) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(MqttConnection)
        }

    override fun deleteMqttConnection(MqttConnection: MqttConnection) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(MqttConnection)
        }
}

interface IMqttConnectionViewModel {
    fun insertMqttConnection(MqttConnection: MqttConnection): Job
    fun updateMqttConnection(MqttConnection: MqttConnection): Job
    fun deleteMqttConnection(MqttConnection: MqttConnection): Job
}