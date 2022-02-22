package com.example.testmc.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.testmc.R
import com.example.testmc.models.MqttConnection
import com.example.testmc.viewmodels.MqttConnectionViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateConnectionActivity : AppCompatActivity() {

    //lateinit var viewModel: MqttConnectionViewModel
    private val viewModel: MqttConnectionViewModel by viewModels()
    var connId: Int = 0

    companion object {
        const val CONN_ID = "conn_id"
        const val CONN_NAME = "conn_name"
        const val CLIENT_ID = "client_id"
        const val BROKER_IP = "broker_ip"
        const val PORT_NUM = "port_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_connection)
/*
        viewModel =  ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            MqttConnectionViewModel::class.java)
*/
        connId = intent.getIntExtra(CONN_ID, 0)
        findViewById<TextInputLayout>(R.id.connNameTxtLayout).editText?.setText(
            intent.getStringExtra(CONN_NAME))
        findViewById<TextInputLayout>(R.id.clientIDTxtLayout).editText?.setText(
            intent.getStringExtra(CLIENT_ID))
        findViewById<TextInputLayout>(R.id.brokerIPTxtLayout).editText?.setText(
            intent.getStringExtra(BROKER_IP))
        findViewById<TextInputLayout>(R.id.portNumTxtLayout).editText?.setText(
            intent.getIntExtra(PORT_NUM, 0).toString())
    }

    fun updateConnection(view: android.view.View) {

        val connName = findViewById<TextInputLayout>(R.id.connNameTxtLayout).editText?.text.toString()
        val clientID = findViewById<TextInputLayout>(R.id.clientIDTxtLayout).editText?.text.toString()
        val brokerIP = findViewById<TextInputLayout>(R.id.brokerIPTxtLayout).editText?.text.toString()
        val portNum = findViewById<TextInputLayout>(R.id.portNumTxtLayout).editText?.text.toString()

        val updMqttConnection = MqttConnection(connName, clientID, brokerIP, portNum.toInt())
        updMqttConnection.id = connId
        viewModel.updateMqttConnection(updMqttConnection)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}