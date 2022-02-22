package com.example.testmc.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testmc.R
import com.example.testmc.adapters.IMqttConnectionRVAdapter
import com.example.testmc.adapters.MqttConnectionRVAdapter
import com.example.testmc.models.MqttConnection
import com.example.testmc.viewmodels.MqttConnectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IMqttConnectionRVAdapter {

    private val viewModel: MqttConnectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.connRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MqttConnectionRVAdapter(this,this)
        recyclerView.adapter = adapter

        //viewModel =  ViewModelProvider(this).get(MqttConnectionViewModel::class.java)

        viewModel.allConnections.observe(this, {list ->
            list.let {
                adapter.updateList(it)
            }
        })
    }

    fun createNewConnection(view: android.view.View) {
        val intent = Intent(this, AddConnectionActivity::class.java)
        startActivity(intent)
    }

    override fun onEditItemClick(MqttConnection: MqttConnection) {
        Toast.makeText(this, "${MqttConnection.connName} View Pressed", Toast.LENGTH_LONG).show()
        val intent =  Intent(this, UpdateConnectionActivity::class.java)
        intent.putExtra(UpdateConnectionActivity.CONN_ID, MqttConnection.id)
        intent.putExtra(UpdateConnectionActivity.CONN_NAME, MqttConnection.connName)
        intent.putExtra(UpdateConnectionActivity.CLIENT_ID, MqttConnection.clientID)
        intent.putExtra(UpdateConnectionActivity.BROKER_IP, MqttConnection.brokerIP)
        intent.putExtra(UpdateConnectionActivity.PORT_NUM, MqttConnection.portNum)
        startActivity(intent)
    }

    override fun onItemClick(MqttConnection: MqttConnection) {
        Toast.makeText(this, "${MqttConnection.connName} View Pressed", Toast.LENGTH_LONG).show()
        val intent =  Intent(this, MqttPanellActivityRv::class.java)
        intent.putExtra(UpdateConnectionActivity.CONN_ID, MqttConnection.id)
        intent.putExtra(UpdateConnectionActivity.CONN_NAME, MqttConnection.connName)
        intent.putExtra(UpdateConnectionActivity.CLIENT_ID, MqttConnection.clientID)
        intent.putExtra(UpdateConnectionActivity.BROKER_IP, MqttConnection.brokerIP)
        intent.putExtra(UpdateConnectionActivity.PORT_NUM, MqttConnection.portNum)
        startActivity(intent)
    }
}