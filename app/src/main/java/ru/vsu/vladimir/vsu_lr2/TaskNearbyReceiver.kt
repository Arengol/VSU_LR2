package ru.vsu.vladimir.vsu_lr2

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TaskNearbyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.ACTION_TASK_NEARBY") {
            val triggeredTask = TaskRepository.loadTasks(context).firstOrNull()

            triggeredTask?.let {
                sendNotification(context, it)
                openMapFragment(context, it)
            }
        }
    }

    private fun sendNotification(context: Context, task: Task) {
        val notificationId = 1

        val notification = NotificationCompat.Builder(context, "task_channel")
            .setContentTitle("Задача рядом!")
            .setContentText("Задача находится в пределах 100 метров.")
            .setSmallIcon(R.drawable.planet)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(context, task))
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


    private fun getPendingIntent(context: Context, task: Task): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = "com.example.ACTION_TASK_NEARBY"
            putExtra("triggered_task", task)
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openMapFragment(context: Context, task: Task) {
        if (context is AppCompatActivity) {
            val fragment = MapFragment.newInstance(task)
            context.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}


