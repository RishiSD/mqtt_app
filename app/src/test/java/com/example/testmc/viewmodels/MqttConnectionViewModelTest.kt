package com.example.testmc.viewmodels

import android.os.Debug
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testmc.getOrAwaitValueTest
import com.example.testmc.models.MqttConnection
import com.example.testmc.repositories.FakeMqttConnRepo
import com.example.testmc.repositories.IMqttConnectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class MqttConnectionViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertMqttConnection() = runTest {
        val viewModel = MqttConnectionViewModel(FakeMqttConnRepo())
        val mqttConn = MqttConnection("testConn1",
            "testClient", "test.mqtt.broker", 1883)
        viewModel.insertMqttConnection(mqttConn)
        delay(1000)

        MatcherAssert.assertThat("insert",
            viewModel.allConnections.getOrAwaitValueTest().firstOrNull { it.connName == "testConn1" } == mqttConn)
    }

    @Test
    fun updateMqttConnection() = runTest {
        val viewModel = MqttConnectionViewModel(FakeMqttConnRepo())
        val name = "testConn2"
        val mqttConn = viewModel.allConnections.getOrAwaitValueTest().first { it.connName == "testConn0" }
        val updMqttConn = MqttConnection(name, mqttConn.clientID, mqttConn.brokerIP, mqttConn.portNum)
        updMqttConn.id = mqttConn.id

        viewModel.updateMqttConnection(updMqttConn)
        delay(1000)

        MatcherAssert.assertThat("update",
            viewModel.allConnections.getOrAwaitValueTest().firstOrNull() { it.connName == name } != null)
    }

    @Test
    fun deleteMqttConnection() = runTest {
        val viewModel = MqttConnectionViewModel(FakeMqttConnRepo())
        val mqttConn = viewModel.allConnections.getOrAwaitValueTest().first { it.connName == "testConn10" }

        viewModel.deleteMqttConnection(mqttConn)
        delay(1000)

        MatcherAssert.assertThat("delete",
            !viewModel.allConnections.getOrAwaitValueTest().contains(mqttConn)
        )
    }
}