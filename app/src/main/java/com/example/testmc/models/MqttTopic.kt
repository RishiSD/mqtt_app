package com.example.testmc.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TopicType {
    PUB, SUB, PUB_STR
}

@Entity(tableName = "mqtt_topic")
data class MqttTopic(
    @ColumnInfo(name = "topic_name") val topicName: String,
    @ColumnInfo(name = "type") val type: TopicType,
    @ColumnInfo(name = "qos") val qos: Int,
    @ColumnInfo(name = "connId") val connId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}