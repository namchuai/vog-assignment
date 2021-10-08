package com.example.fcmexample

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.SingleLiveEvent
import com.example.fcmexample.utils.TOKEN
import com.example.fcmsender.FCMSender
import com.example.fcmsender.MessageType
import com.example.fcmsender.models.FCMResponse
import kotlinx.coroutines.launch

class SendViewModel(app: Application) : AndroidViewModel(app) {
    val sendNotificationMessage = SingleLiveEvent<String>()

    val sendDataMessage = MutableLiveData(true)
    val title = MutableLiveData<String>()
    val body = MutableLiveData<String>()
    val topic = MutableLiveData<String>()

    private val sharedPreferences by lazy {
        getApplication<BaseApplication>().getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun sendNotification() {
        val title = title.value
        if (title.isNullOrBlank()) {
            sendNotificationMessage.value = "Title is empty"
            return
        }

        val body = body.value
        if (body.isNullOrBlank()) {
            sendNotificationMessage.value = "Please enter body"
            return
        }

        val topic = topic.value
        if (topic.isNullOrBlank()) {
            sendNotificationMessage.value = "Please enter topic"
            return
        }

        val type = if (sendDataMessage.value!!) MessageType.DATA else MessageType.NOTIFICATION
        viewModelScope.launch {
            sharedPreferences.getString(TOKEN, "")?.let {
                if (it.isNotEmpty()) {
                    val response = FCMSender.FCMMessageBuilder()
                        .setMessageType(type)
                        .setTitle(title)
                        .setBody(body)
                        .setTopic(topic)
                        .build()
                        .sendTo(it)
                    onResponse(response)
                }
            }
        }
    }

    private fun onResponse(response: FCMResponse) {
        if (response.success == 1) {
            sendNotificationMessage.value = "Notification sent!"
            return
        }

        sendNotificationMessage.value = "Notification failed to send!"
    }
}