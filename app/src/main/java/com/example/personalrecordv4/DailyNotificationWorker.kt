package com.example.personalrecordv4

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.personalrecordv4.viewmodel.NotificationViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class DailyNotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    var notificationViewModel = NotificationViewModel()

    override suspend fun doWork(): Result {
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:MM"))
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        if (notificationViewModel.days.value!!.contains(today) && notificationViewModel.notifications.value!!.time==currentTime){

            return Result.success()
        }
        return Result.retry()
    }
    private fun sendPushNotification(token: String, message: String){

    }

    private fun createNotificationMessage(day: Int): String {
        when(day){
            Calendar.MONDAY -> return "It's Monday! Ready to Workout?"
            Calendar.TUESDAY -> return "It's Tuesday! Ready to Workout?"
            Calendar.WEDNESDAY -> return "It's Wednesday! Ready to Workout?"
            Calendar.THURSDAY -> return "It's Thursday! Ready to Workout?"
            Calendar.FRIDAY -> return "It's Friday! Ready to Workout?"
            Calendar.SATURDAY -> return "It's Saturday! Ready to Workout?"
            Calendar.SUNDAY -> return "It's Sunday! Ready to Workout?"
            else -> return "Ready to Workout Today?"
        }
    }
}