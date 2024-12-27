package ru.vsu.vladimir.vsu_lr2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.sqrt

class MapFragment : Fragment() {

    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: TaskAdapter
    private var taskList = mutableListOf<Task>()
    private var referenceTask: Task? = null
    private lateinit var mapTitleTextView: TextView

    companion object {
        private const val ARG_REFERENCE_TASK = "reference_task"

        fun newInstance(referenceTask: Task): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putParcelable(ARG_REFERENCE_TASK, referenceTask)  // Передаем Parcelable объект
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_list, container, false)

        taskListView = view.findViewById(R.id.mapListView)
        mapTitleTextView = view.findViewById(R.id.mapTitle)
        referenceTask = arguments?.getParcelable(ARG_REFERENCE_TASK)
        referenceTask?.let {
            mapTitleTextView.text = "Задача: ${it.description}"
        }
        loadAndSortTasks()
        taskAdapter = TaskAdapter(requireContext(), taskList)
        taskListView.adapter = taskAdapter

        return view
    }

    private fun loadAndSortTasks() {
        val allTasks = TaskRepository.loadTasks(requireContext())
        taskList = allTasks.filter { it != referenceTask }
            .sortedBy { task ->
                referenceTask?.let {
                    calculateDistance(it.latitude, it.longitude, task.latitude, task.longitude)
                } ?: Double.MAX_VALUE
            }.toMutableList()
        referenceTask?.let {
            taskList.add(0, it)
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val deltaX = lon2 - lon1
        val deltaY = lat2 - lat1
        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }
}



