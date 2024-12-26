package ru.vsu.vladimir.vsu_lr2

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskRepository {

    private const val PREFS_NAME = "task_prefs"
    private const val KEY_TASK_LIST = "task_list"

    // Сохраняем список задач в SharedPreferences
    fun saveTasks(context: Context, tasks: List<Task>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(tasks)
        editor.putString(KEY_TASK_LIST, json)
        editor.apply()
    }

    // Получаем список задач из SharedPreferences
    fun loadTasks(context: Context): List<Task> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_TASK_LIST, "[]")
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(json, type)
    }
}
