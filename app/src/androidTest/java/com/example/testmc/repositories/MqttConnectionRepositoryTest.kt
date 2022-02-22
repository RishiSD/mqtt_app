package com.example.testmc.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.example.testmc.data.MqttConnectionDao
import com.example.testmc.data.MqttConnectionDatabase
import com.example.testmc.models.MqttConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MqttConnectionRepositoryTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MqttConnectionDatabase
    private lateinit var dao: MqttConnectionDao
    private lateinit var mqttConnRepo: MqttConnectionRepository

    @Before
    fun setup() {
        database =  Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MqttConnectionDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getMqttConnectionDao()
        mqttConnRepo = MqttConnectionRepository(dao)
        val mqttConn = MqttConnection("testConn1",
            "testClient", "test.mqtt.broker", 1883)
        runBlocking {
            dao.insert(mqttConn)
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert() = runTest {
        val mqttConn = MqttConnection("testConn2",
            "testClient", "test.mqtt.broker", 1883)
        mqttConnRepo.insert(mqttConn)

        val allConnections = mqttConnRepo.allConnections.getOrAwaitValue()

        MatcherAssert.assertThat("insert", allConnections.contains(mqttConn))
    }

    @Test
    fun update() = runTest {
        var allConnections = mqttConnRepo.allConnections.getOrAwaitValue()
        var conn = allConnections[0]

        val name2 = "testConn2"
        val updConn = MqttConnection(name2,
            "testClient", "test.mqtt.broker", 1883)
        updConn.id = conn.id
        mqttConnRepo.update(updConn)

        allConnections = mqttConnRepo.allConnections.getOrAwaitValue()
        conn = allConnections[0]
        MatcherAssert.assertThat("update", conn?.connName == name2)
    }

    @Test
    fun delete() = runTest {
        var allConnections = mqttConnRepo.allConnections.getOrAwaitValue()
        var conn = allConnections[0]
        mqttConnRepo.delete(conn)

        allConnections = mqttConnRepo.allConnections.getOrAwaitValue()
        MatcherAssert.assertThat(
            "delete",
            !allConnections.contains(conn)
        )
    }
}