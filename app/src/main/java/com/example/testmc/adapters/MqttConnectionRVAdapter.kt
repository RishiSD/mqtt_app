package com.example.testmc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testmc.R
import com.example.testmc.models.MqttConnection


class MqttConnectionRVAdapter(private val context: Context, private val listener: IMqttConnectionRVAdapter):
    RecyclerView.Adapter<MqttConnectionRVAdapter.MqttConnectionViewHolder>() {

    private val allConnections = ArrayList<MqttConnection>()

    inner class MqttConnectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById<TextView>(R.id.text)
        val editButton: ImageView = itemView.findViewById<ImageView>(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MqttConnectionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mqtt_conn, parent, false)
        val viewHolder = MqttConnectionViewHolder(view)
        viewHolder.editButton.setOnClickListener {
            listener.onEditItemClick(allConnections[viewHolder.adapterPosition])
        }
        view.setOnClickListener {
            listener.onItemClick(allConnections[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MqttConnectionViewHolder, position: Int) {
        val currentConnection = allConnections[position]
        holder.textView.text = currentConnection.connName
    }

    override fun getItemCount(): Int {
        return allConnections.size
    }

    fun updateList(newList: List<MqttConnection>) {
        allConnections.clear()
        allConnections.addAll(newList)

        notifyDataSetChanged()
    }
}

interface IMqttConnectionRVAdapter {
    fun onEditItemClick(MqttConnection: MqttConnection)
    fun onItemClick(MqttConnection: MqttConnection)
}