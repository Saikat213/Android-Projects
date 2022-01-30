package com.example.chatapplication.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants.CHANNEL_ID
import com.example.chatapplication.view.MainActivity
import kotlin.random.Random

class MyWorker(val context: Context, workerParams : WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val taskData = inputData.keyValueMap
        val message = taskData.getValue("message")
        scheduleNotifications(message.toString(), context)
        return Result.success()
    }

    private fun scheduleNotifications(message : String, context: Context) {
        val notificationManager : NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        //val notificationID = Random.nextInt()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_ONE_SHOT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel : NotificationChannel = NotificationChannel(CHANNEL_ID, "NewMessage", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("RetrofitTest").setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications).setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, builder.build())
    }
}