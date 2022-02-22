package com.example.testmc.network

import android.content.Context
import android.util.Log
import com.example.testmc.models.MqttConnection
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MqttClientHelper(context: Context?, MqttConnection: MqttConnection) {

    companion object {
        const val TAG = "MqttClientHelper"
    }

    var mqttCon: MqttConnection = MqttConnection
    val brokerUri = "tcp://" + mqttCon.brokerIP + ":" + mqttCon.portNum
    var mqttAndroidClient: MqttAndroidClient = MqttAndroidClient(context, brokerUri, mqttCon.clientID)

    fun setCallback(callback: MqttCallbackExtended?) {
        mqttAndroidClient.setCallback(callback)
    }

    init {
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w(TAG, s)
            }

            override fun connectionLost(throwable: Throwable) {}
            @Throws(Exception::class)
            override fun messageArrived(
                topic: String,
                mqttMessage: MqttMessage
            ) {
                Log.w(TAG, mqttMessage.toString())
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
        })
        connect()
    }

    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        mqttConnectOptions.userName = "test"
        mqttConnectOptions.password = "test".toCharArray()
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    val disconnectedBufferOptions =
                        DisconnectedBufferOptions()
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.w(TAG, "Failed to connect to: ${mqttCon.brokerIP} ; $exception")
                }
            })
        } catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }

    fun subscribe(subscriptionTopic: String, qos: Int = 0) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.w(TAG, "Subscribed to topic '$subscriptionTopic'")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.w(TAG, "Subscription to topic '$subscriptionTopic' failed!")
                }
            })
        } catch (ex: MqttException) {
            System.err.println("Exception whilst subscribing to topic '$subscriptionTopic'")
            ex.printStackTrace()
        }
    }

    fun publish(topic: String, msg: String, qos: Int = 0) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            mqttAndroidClient.publish(topic, message.payload, qos, false)
            Log.d(TAG, "Message published to topic `$topic`: $msg")
        } catch (e: MqttException) {
            Log.d(TAG, "Error Publishing to $topic: " + e.message)
            e.printStackTrace()
        }

    }

    fun isConnected() : Boolean {
        return mqttAndroidClient.isConnected
    }

    fun destroy() {
        mqttAndroidClient.unregisterResources()
        mqttAndroidClient.disconnect()
    }
}
