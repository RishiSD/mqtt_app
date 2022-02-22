package com.example.testmc.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import com.example.testmc.R
import com.example.testmc.models.MqttTopic
import com.example.testmc.models.TopicType
import com.example.testmc.viewmodels.MqttConnectionViewModel
import com.example.testmc.viewmodels.TopicViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTopicActivity : AppCompatActivity() {

    private val viewModel: TopicViewModel by viewModels()

    var topicType: TopicType = TopicType.PUB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)
    }

    fun saveTopic(view: android.view.View) {
        val topicName = findViewById<TextInputLayout>(R.id.topNameTxtLayout).editText?.text.toString()
        viewModel.insertMqttTopic(MqttTopic(topicName, topicType, 0, 1))
        finish()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_publish ->
                    if (checked) {
                        topicType = TopicType.PUB
                    }
                R.id.radio_subscribe ->
                    if (checked) {
                        topicType = TopicType.SUB
                    }
                R.id.radio_pubstr ->
                    if (checked) {
                        topicType = TopicType.PUB_STR
                    }
            }
        }
    }
}