package com.example.testmc.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.testmc.R
import com.example.testmc.models.MqttConnection
import com.example.testmc.network.MqttClientHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttPanellActivity : AppCompatActivity() {

    var connId: Int = 0
    var job: Job? = null
    lateinit var MqttConnection : MqttConnection
    lateinit var mqttClient: MqttClientHelper

    companion object {
        const val CONN_ID = "conn_id"
        const val CONN_NAME = "conn_name"
        const val CLIENT_ID = "client_id"
        const val BROKER_IP = "broker_ip"
        const val PORT_NUM = "port_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mqtt_panell)

        MqttConnection = MqttConnection(
            intent.getStringExtra(UpdateConnectionActivity.CONN_NAME)!!,
            intent.getStringExtra(UpdateConnectionActivity.CLIENT_ID)!!,
            intent.getStringExtra(UpdateConnectionActivity.BROKER_IP)!!,
            intent.getIntExtra(UpdateConnectionActivity.PORT_NUM, 0)
        )
        MqttConnection.id = intent.getIntExtra(UpdateConnectionActivity.CONN_ID, 0)
        mqttClient = MqttClientHelper(this, MqttConnection)
        setMqttCallBack()
    }

    private fun setMqttCallBack() {
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                val snackbarMsg = "Connected to host:\n'${MqttConnection.brokerIP}'."
                Log.w("Debug", snackbarMsg)
                Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                mqttClient.subscribe("test")
            }
            override fun connectionLost(throwable: Throwable) {
                val snackbarMsg = "Connection to host lost:\n'${MqttConnection.brokerIP}'"
                Log.w("Debug", snackbarMsg)
                Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", "Message received from host '${MqttConnection.brokerIP}': $mqttMessage")
                findViewById<EditText>(R.id.subscribedText).setText(mqttMessage.toString())
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", "Message published to host '${MqttConnection.brokerIP}'")
            }
        })
    }

    override fun onDestroy() {
        mqttClient.destroy()
        super.onDestroy()
    }

    fun publish(view: android.view.View) {
        val message = findViewById<EditText>(R.id.publishText).text.toString()
        try {
            mqttClient.publish("test", message)
        } catch (ex: MqttException) {
            val snackbarMsg = "Publish failed on topic test"
            Log.w("Debug", snackbarMsg)
            Snackbar.make(findViewById(android.R.id.content), snackbarMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun publishStream(view: android.view.View) {
        val loTrsh = findViewById<EditText>(R.id.lowerValue).text.toString().toInt()
        val hiTrsh = findViewById<EditText>(R.id.upperValue).text.toString().toInt()
        val delaySec = findViewById<EditText>(R.id.delay).text.toString().toDouble()
        Log.d("RISHI", "delaySec : $delaySec")
        job =  lifecycleScope.launch(Dispatchers.Main) {
            while(true) {
                if (isActive) {
                    try {
                        mqttClient.publish("test", (loTrsh..hiTrsh).random().toString())
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

    fun stopPublishStream(view: android.view.View) {
        job?.cancel()
    }
}