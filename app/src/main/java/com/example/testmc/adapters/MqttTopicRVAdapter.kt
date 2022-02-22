package com.example.testmc.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testmc.R
import com.example.testmc.models.MqttTopic
import com.example.testmc.models.TopicType
import com.example.testmc.network.MqttClientHelper
import java.util.*
import kotlin.collections.ArrayList


class MqttTopicRVAdapter(private val context: Context, private val listener: IMqttTopicRVAdapter):
    RecyclerView.Adapter<MqttTopicRVAdapter.DataAdapterViewHolder>() {

    private val allTopics = ArrayList<MqttTopic>()

    companion object {
        private const val PUB = 0
        private const val SUB = 1
        private const val PUB_STR = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {

        val layout = when (viewType) {
            PUB -> R.layout.item_mqtt_topic
            SUB -> R.layout.item_mqtt_topic_sub
            PUB_STR -> R.layout.item_mqtt_topic_pubstr
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return DataAdapterViewHolder(view, allTopics, listener)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(allTopics[position])
    }

    class DataAdapterViewHolder(itemView: View, private val allTopics: ArrayList<MqttTopic>,
                                private val listener: IMqttTopicRVAdapter) : RecyclerView.ViewHolder(itemView) {

        private fun bindPub(item: MqttTopic) {
            //Do your view assignment here from the data model

            itemView.findViewById<TextView>(R.id.title).text = "Publish - Topic: ${item.topicName}"
            val publishEditText: EditText = itemView.findViewById<EditText>(R.id.publishText)
            val publishBtn: Button =  itemView.findViewById<Button>(R.id.publishBtn)
            publishBtn.setOnClickListener{
                listener.onPublish(allTopics[adapterPosition], publishEditText.text)
                publishEditText.text.clear()
            }
            val deleteBtn: ImageView =  itemView.findViewById<ImageView>(R.id.deleteButton)
            deleteBtn.setOnClickListener {
                listener.deleteTopic(allTopics[adapterPosition])
            }

        }

        private fun bindSub(item: MqttTopic) {
            itemView.findViewById<TextView>(R.id.title).text = "Subscribe - Topic: ${item.topicName}"
            val subscribeTextView: TextView = itemView.findViewById<TextView>(R.id.subscribedText)
            listener.setSubscribeCallback(allTopics[adapterPosition], subscribeTextView)
            val deleteBtn: ImageView =  itemView.findViewById<ImageView>(R.id.deleteButton)
            deleteBtn.setOnClickListener {
                listener.deleteTopic(allTopics[adapterPosition])
            }
        }

        private fun bindPubStream(item: MqttTopic) {
            itemView.findViewById<TextView>(R.id.title).text = "PubStream - Topic: ${item.topicName}"

            val startBtn: Button =  itemView.findViewById<Button>(R.id.startBtn)
            val stopBtn: Button =  itemView.findViewById<Button>(R.id.stopBtn)
            val uuid = UUID.randomUUID()
            startBtn.setOnClickListener {
                val loTrsh = itemView.findViewById<EditText>(R.id.lowerValue).text.toString().toInt()
                val hiTrsh = itemView.findViewById<EditText>(R.id.upperValue).text.toString().toInt()
                val delaySec = itemView.findViewById<EditText>(R.id.delay).text.toString().toDouble()
                listener.onStartPublishStream(item, loTrsh, hiTrsh, delaySec, uuid)
            }
            stopBtn.setOnClickListener {
                listener.onStopPublishStream(uuid)
            }
            val deleteBtn: ImageView =  itemView.findViewById<ImageView>(R.id.deleteButton)
            deleteBtn.setOnClickListener {
                listener.deleteTopic(allTopics[adapterPosition])
            }
        }

        fun bind(mqttTopic: MqttTopic) {
            if (mqttTopic.type == TopicType.PUB) {
                return bindPub(mqttTopic)
            }
            else if (mqttTopic.type == TopicType.SUB) {
                return bindSub(mqttTopic)
            }
            else if (mqttTopic.type == TopicType.PUB_STR) {
                return bindPubStream(mqttTopic)
            }

            return bindSub(mqttTopic)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(allTopics[position].type){
            TopicType.PUB -> PUB
            TopicType.SUB -> SUB
            TopicType.PUB_STR -> PUB_STR
            else -> SUB
        }
    }

    override fun getItemCount(): Int {
        return allTopics.size
    }

    fun updateList(newList: List<MqttTopic>) {
        allTopics.clear()
        allTopics.addAll(newList)

        notifyDataSetChanged()
    }
}

interface IMqttTopicRVAdapter {
    fun onPublish(mqttTopic: MqttTopic, publishText: CharSequence)
    fun setSubscribeCallback(mqttTopic: MqttTopic, textView: TextView)
    fun onStartPublishStream(mqttTopic: MqttTopic, loTrsh: Int,
                             hiTrsh: Int, delaySec: Double, uuid: UUID)
    fun onStopPublishStream(uuid: UUID)
    fun deleteTopic(mqttTopic: MqttTopic)
}