package com.example.testmc.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mqtt_connection")
data class MqttConnection(
    @ColumnInfo(name = "conn_name") val connName: String,
    @ColumnInfo(name = "client_id") val clientID: String,
    @ColumnInfo(name = "broker_ip") val brokerIP: String,
    @ColumnInfo(name = "port_number") val portNum: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}