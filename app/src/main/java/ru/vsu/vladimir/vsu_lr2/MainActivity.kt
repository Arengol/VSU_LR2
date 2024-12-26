package ru.vsu.vladimir.vsu_lr2

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TaskListFragment())
                .commit()
        }
    }

    private lateinit var taskNearbyReceiver: TaskNearbyReceiver

    override fun onStart() {
        super.onStart()
        taskNearbyReceiver = TaskNearbyReceiver()
        val filter = IntentFilter("com.example.ACTION_TASK_NEARBY")
        LocalBroadcastManager.getInstance(this).registerReceiver(taskNearbyReceiver, filter)
        val intent = Intent(this, LocationService::class.java)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(taskNearbyReceiver)
    }
}