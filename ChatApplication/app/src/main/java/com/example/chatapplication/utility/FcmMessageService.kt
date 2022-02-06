package com.example.chatapplication.utility

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmMessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM--->", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification!!.body!!.isNotEmpty()) {
            val message = mutableMapOf<String, String>()
            message.put("SenderID", remoteMessage.notification!!.title!!)
            message.put("message", remoteMessage.notification!!.body!!)
            sendNotifications(message, applicationContext)
        }
    }

    private fun sendNotifications(message : MutableMap<String, String>, context: Context) {
        val jobRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(workDataOf("SenderID" to message.get("SenderID"),
                "message" to message.get("message"))).build()
        WorkManager.getInstance(context).enqueue(jobRequest)
    }
}