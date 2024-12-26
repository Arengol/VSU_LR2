package ru.vsu.vladimir.vsu_lr2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import kotlin.math.sqrt

class MapFragment : Fragment() {

    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: TaskAdapter

    // Список задач, которые мы будем показывать
    private var taskList = mutableListOf<Task>()

    // Задача, которая вызвала уведомление
    private var triggeringTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        taskListView = view.findViewById(R.id.taskListView)

        // Получаем задачу, которая вызвала уведомление, из аргументов
        triggeringTask = arguments?.getParcelable("triggeringTask")

        triggeringTask?.let {
            // Добавляем задачу, которая вызвала уведомление, на первое место
            taskList.add(it)
            // Загружаем все задачи из репозитория
            val allTasks = TaskRepository.loadTasks(requireContext())
            // Отсортируем задачи по расстоянию от "triggeringTask"
            val sortedTasks = allTasks.sortedBy { task ->
                calculateDistance(it.latitude, it.longitude, task.latitude, task.longitude)
            }
            // Добавляем в список 4 ближайшие задачи
            taskList.addAll(sortedTasks.take(4))
        }

        taskAdapter = TaskAdapter(requireContext(), taskList)
        taskListView.adapter = taskAdapter

        return view
    }

    // Функция для вычисления расстояния между двумя координатами (Haversine)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val deltaX = lon2 - lon1
        val deltaY = lat2 - lat1
        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    companion object {
        fun newInstance(task: Task): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putParcelable("triggeringTask", task)
            fragment.arguments = args
            return fragment
        }
    }
}
