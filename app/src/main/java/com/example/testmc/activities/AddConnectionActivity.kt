package com.example.testmc.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.testmc.R
import com.example.testmc.models.MqttConnection
import com.example.testmc.viewmodels.MqttConnectionViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddConnectionActivity : AppCompatActivity() {

    //lateinit var viewModel: MqttConnectionViewModel
    private val viewModel: MqttConnectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)
/*
        viewModel =  ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MqttConnectionViewModel::class.java)
*/
    }

    fun saveConnection(view: android.view.View) {

        val connName = findViewById<TextInputLayout>(R.id.connNameTxtLayout).editText?.text.toString()
        val clientID = findViewById<TextInputLayout>(R.id.clientIDTxtLayout).editText?.text.toString()
        val brokerIP = findViewById<TextInputLayout>(R.id.brokerIPTxtLayout).editText?.text.toString()
        val portNum = findViewById<TextInputLayout>(R.id.portNumTxtLayout).editText?.text.toString()
        viewModel.insertMqttConnection(MqttConnection(connName, clientID, brokerIP, portNum.toInt()))
        Toast.makeText(this, "$connName Inserted", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}