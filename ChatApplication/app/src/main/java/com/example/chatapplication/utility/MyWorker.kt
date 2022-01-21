package com.example.chatapplication.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.chatapplication.R

class MyWorker(context: Context, workerParams : WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val taskData = inputData.keyValueMap
        val userPhn = taskData.getValue("UserPhoneNumber")
        val message = taskData.getValue("LastMessage")
        scheduleNotifications(userPhn.toString(), message.toString())
        return Result.success()
    }

    private fun scheduleNotifications(userPhn : String, message : String) {
        val notificationManager : NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel : NotificationChannel = NotificationChannel("Reminder", "NewMessage", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, "Reminder")
            .setContentTitle(userPhn).setContentText(message).setSmallIcon(R.drawable.ic_notifications)

        notificationManager.notify(1, builder.build())
    }
}