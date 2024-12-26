package ru.vsu.vladimir.vsu_lr2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlin.random.Random
import kotlin.math.*

class LocationService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val taskList = mutableListOf<Task>()  // Сюда добавим текущий список задач

    private val updateInterval = 10000L  // Каждые 10 секунд

    override fun onCreate() {
        super.onCreate()
        // Загружаем задачи из репозитория
        taskList.addAll(TaskRepository.loadTasks(applicationContext))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Каждые 10 секунд генерируем новые координаты
        handler.postDelayed(object : Runnable {
            override fun run() {
                generateRandomCoordinates()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // Функция для генерации случайных координат
    private fun generateRandomCoordinates() {
        val latitude = Random.nextDouble(0.0, 100.0)
        val longitude = Random.nextDouble(0.0, 100.0)

        checkDistanceAndSendBroadcast(80.0, 41.0)
    }

    // Функция для вычисления расстояния между двумя координатами (с использованием формулы Haversine)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val deltaX = lon2 - lon1
        val deltaY = lat2 - lat1
        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    // Проверка расстояния и отправка широковещательного сообщения
    private fun checkDistanceAndSendBroadcast(lat: Double, lon: Double) {
        for (task in taskList) {
            val distance = calculateDistance(lat, lon, task.latitude, task.longitude)
            Log.d("DISTANCE", distance.toString())
            if (distance <= 100) {  // Если задача в пределах 100 метров
                sendBroadcastToReceiver()
                break  // Останавливаем цикл, если нашли задачу
            }
        }
    }

    // Отправка широковещательного сообщения
    private fun sendBroadcastToReceiver() {
        val intent = Intent("com.example.ACTION_TASK_NEARBY")  // Уникальное действие для широковещательного сообщения
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
