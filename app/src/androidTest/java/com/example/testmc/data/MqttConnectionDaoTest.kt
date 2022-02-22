package com.example.testmc.data

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.example.testmc.models.MqttConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MqttConnectionDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MqttConnectionDatabase
    private lateinit var dao: MqttConnectionDao
    private  lateinit var mqttConn: MqttConnection

    @Before
    fun setup() {
        database =  Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MqttConnectionDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getMqttConnectionDao()

        mqttConn = MqttConnection("testConn1",
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
        dao.insert(mqttConn)

        val allConnections = dao.getAllConnections().getOrAwaitValue()

        assertThat("insert", allConnections.contains(mqttConn))
    }

    @Test
    fun update() = runTest {
        var allConnections = dao.getAllConnections().getOrAwaitValue()
        var conn = allConnections[0]

        val name2 = "testConn2"
        val updConn = MqttConnection(name2,
            "testClient", "test.mqtt.broker", 1883)
        updConn.id = conn.id
        dao.update(updConn)

        allConnections = dao.getAllConnections().getOrAwaitValue()
        conn = allConnections[0]
        assertThat("update", conn?.connName == name2)
    }

    @Test
    fun delete() = runTest {
        var allConnections = dao.getAllConnections().getOrAwaitValue()
        var conn = allConnections[0]
        dao.delete(conn)

        allConnections = dao.getAllConnections().getOrAwaitValue()
        assertThat("delete",
            !allConnections.contains(mqttConn))
    }
}