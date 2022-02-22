package com.example.testmc.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testmc.R
import com.example.testmc.adapters.IMqttTopicRVAdapter
import com.example.testmc.adapters.MqttConnectionRVAdapter
import com.example.testmc.adapters.MqttTopicRVAdapter
import com.example.testmc.databinding.ActivityMqttPanellRvBinding
import com.example.testmc.models.MqttConnection
import com.example.testmc.models.MqttTopic
import com.example.testmc.models.TopicType
import com.example.testmc.network.MqttClientHelper
import com.example.testmc.viewmodels.MqttConnectionViewModel
import com.example.testmc.viewmodels.TopicViewModel
import dagger.hilt.android.AndroidEntryPoint
import junit.runner.Version.id
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class MqttPanellActivityRv : AppCompatActivity(), IMqttTopicRVAdapter {

    private val viewModel: TopicViewModel by viewModels()
    lateinit var mqttConnection : MqttConnection
    lateinit var mqttClient: MqttClientHelper
    var jobs = HashMap<UUID, Job>()
    var subscribed_topics = HashMap<String, TextView>()

    companion object {
        const val CONN_ID = "conn_id"
        const val CONN_NAME = "conn_name"
        const val CLIENT_ID = "client_id"
        const val BROKER_IP = "broker_ip"
        const val PORT_NUM = "port_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mqtt_panell_rv)

        mqttConnection = MqttConnection(
            intent.getStringExtra(UpdateConnectionActivity.CONN_NAME)!!,
            intent.getStringExtra(UpdateConnectionActivity.CLIENT_ID)!!,
            intent.getStringExtra(UpdateConnectionActivity.BROKER_IP)!!,
            intent.getIntExtra(UpdateConnectionActivity.PORT_NUM, 0)
        )
        mqttConnection.id = intent.getIntExtra(UpdateConnectionActivity.CONN_ID, 0)
        mqttClient = MqttClientHelper(this, mqttConnection)

        val recyclerView = findViewById<RecyclerView>(R.id.topicRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MqttTopicRVAdapter(this,this)
        recyclerView.adapter = adapter

        //viewModel =  ViewModelProvider(this).get(MqttConnectionViewModel::class.java)

        viewModel.allTopics.observe(this, {list ->
            list.let {
                adapter.updateList(it)
            }
        })
    }

    override fun onPublish(mqttTopic: MqttTopic, publishText: CharSequence) {
        Toast.makeText(this, "${publishText} sent", Toast.LENGTH_LONG).show()
        val message = publishText.toString()
        try {
            Log.w("Debug", "Before client call")
            mqttClient.publish(mqttTopic.topicName, message)
        } catch (ex: MqttException) {
            val snackbarMsg = "Publish failed on topic test"
            Log.w("Debug", snackbarMsg)
            Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun setSubscribeCallback(mqttTopic: MqttTopic, textView: TextView) {

        subscribed_topics[mqttTopic.topicName] = textView
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                val snackbarMsg = "Connected to host:\n'${mqttConnection.brokerIP}'."
                Log.w("Debug", snackbarMsg)
                Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                mqttClient.subscribe(mqttTopic.topicName)
            }
            override fun connectionLost(throwable: Throwable) {
                val snackbarMsg = "Connection to host lost:\n'${mqttConnection.brokerIP}'"
                Log.w("Debug", snackbarMsg)
                Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", "Message received from host '${mqttConnection.brokerIP}': $mqttMessage")
                textView.text = mqttMessage.toString()
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", "Message published to host '${mqttConnection.brokerIP}'")
            }
        })
    }

    override fun onStartPublishStream(mqttTopic: MqttTopic, loTrsh: Int, hiTrsh: Int, delaySec: Double, uuid: UUID) {
        Log.d("RISHI", "delaySec : $delaySec")
        jobs[uuid] =  lifecycleScope.launch(Dispatchers.Main) {
            while(true) {
                if (isActive) {
                    try {
                        mqttClient.publish(mqttTopic.topicName, (loTrsh..hiTrsh).random().toString())
                        Log.d("RISHI", "published")
                    } catch (ex: MqttException) {
                        val snackbarMsg = "Publish failed on topic test"
                        Log.d("RISHI", snackbarMsg)
                        Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
                else {
                    break
                }
                Log.d("RISHI", "converted ${(delaySec * 1000L).toLong()}")
                delay((delaySec * 1000L).toLong())
            }
        }
    }

    override fun onStopPublishStream(uuid: UUID) {
        val job = jobs.remove(uuid)
        job?.cancel()
    }

    override fun deleteTopic(mqttTopic: MqttTopic) {
        viewModel.deleteMqttTopic(mqttTopic)
    }

    fun createNewTopic(view: android.view.View) {
        val intent = Intent(this, AddTopicActivity::class.java)
        startActivity(intent)
    }
}