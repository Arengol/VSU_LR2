package ru.vsu.vladimir.vsu_lr2
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel",
                "Задачи рядом",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Канал уведомлений о задачах"
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}