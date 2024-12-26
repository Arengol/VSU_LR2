package ru.vsu.vladimir.vsu_lr2

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TaskNearbyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.ACTION_TASK_NEARBY") {
            sendNotification(context)
        }
    }

    // Отправка локального уведомления
    private fun sendNotification(context: Context) {
        val notificationId = 1

        val notification = NotificationCompat.Builder(context, "task_channel")
            .setContentTitle("Задача рядом!")
            .setContentText("Задача находится в пределах 100 метров.")
            .setSmallIcon(R.drawable.planet)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ERROR", "permission Error")
            return
        }
        notificationManager.notify(notificationId, notification)
    }
}
