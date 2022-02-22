package com.example.testmc.data

import android.os.Debug
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.example.testmc.models.MqttConnection
import com.example.testmc.models.MqttTopic
import com.example.testmc.models.TopicType
import com.example.testmc.repositories.MqttTopicRepository
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
class MqttTopicRepositoryTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MqttConnectionDatabase
    private lateinit var dao: MqttTopicDao
    private lateinit var mqttTopicRepo: MqttTopicRepository

    @Before
    fun setup() {
        database =  Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MqttConnectionDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getMqttTopicDao()
        mqttTopicRepo =  MqttTopicRepository(dao)

        val mqttTopic = MqttTopic("sensors01", TopicType.PUB, 0, 1)
        runBlocking {
            dao.insert(mqttTopic)
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert() = runTest {
        val mqttTop = MqttTopic("sensors02", TopicType.PUB, 0, 1)
        mqttTopicRepo.insert(mqttTop)

        val allTopics = mqttTopicRepo.allTopics.getOrAwaitValue()
        assertThat("insert", allTopics.contains(mqttTop))
    }

    @Test
    fun update() = runTest {
        var allTopics = mqttTopicRepo.allTopics.getOrAwaitValue()
        var topic = allTopics[0]

        val name2 = "sensors02"
        val updTop = MqttTopic(name2, TopicType.PUB, 0, 1)
        updTop.id = topic.id
        mqttTopicRepo.update(updTop)

        allTopics = mqttTopicRepo.allTopics.getOrAwaitValue()
        topic = allTopics[0]
        assertThat("update", topic?.topicName == name2)
    }

    @Test
    fun delete() = runTest {
        var allTopics = mqttTopicRepo.allTopics.getOrAwaitValue()
        var topic = allTopics[0]
        mqttTopicRepo.delete(topic)

        allTopics = mqttTopicRepo.allTopics.getOrAwaitValue()
        assertThat("delete",
            !allTopics.contains(topic))
    }
}