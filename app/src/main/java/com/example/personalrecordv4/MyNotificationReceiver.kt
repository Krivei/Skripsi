package com.example.personalrecordv4

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class MyNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Ashuu","Notification Worked")
        if (intent?.action == "com.pushwoosh.notification"){
            Log.i("Ashuu","Notification Worked")
            val data = intent.extras
            val title = data?.getString("title")
            val message = data?.getString("message")

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(context)
                .setContentText(message)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1,notificationBuilder.build())
        }
    }
}