package com.example.fundooapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmManager : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val builder = NotificationCompat.Builder(context!!, "Reminder")
            .setSmallIcon(R.drawable.ic_reminder).setContentTitle("Reminder Alarm Manager")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(12, builder.build())
    }
}