package ru.vsu.vladimir.vsu_lr2

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class TaskListFragment : Fragment() {

    private lateinit var taskListView: ListView
    private lateinit var addButton: Button
    private lateinit var taskAdapter: TaskAdapter

    private var taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskListView = view.findViewById(R.id.taskListView)
        addButton = view.findViewById(R.id.addTaskButton)

        taskList = TaskRepository.loadTasks(requireContext()).toMutableList()

        taskAdapter = TaskAdapter(requireContext(), taskList)
        taskListView.adapter = taskAdapter

        taskListView.setOnItemClickListener { _, _, position, _ ->
            val task = taskList[position]
            showTaskDialog(task, position)
        }

        addButton.setOnClickListener {
            showTaskDialog(null, -1)
        }

        return view
    }

    // Показать диалог для создания или редактирования задачи
    private fun showTaskDialog(task: Task?, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_task, null)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.descriptionEditText)
        val dateEditText: EditText = dialogView.findViewById(R.id.dateEditText)
        val latEditText: EditText = dialogView.findViewById(R.id.latEditText)
        val lonEditText: EditText = dialogView.findViewById(R.id.lonEditText)

        task?.let {
            descriptionEditText.setText(it.description)
            dateEditText.setText(it.date)
            latEditText.setText(it.latitude.toString())
            lonEditText.setText(it.longitude.toString())
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (task == null) "Добавить задачу" else "Редактировать задачу")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val description = descriptionEditText.text.toString()
                val date = dateEditText.text.toString()
                val latitude = latEditText.text.toString().toDoubleOrNull() ?: 0.0
                val longitude = lonEditText.text.toString().toDoubleOrNull() ?: 0.0

                if (task == null) {
                    val newTask = Task(description, date, latitude, longitude)
                    taskList.add(newTask)
                } else {
                    val updatedTask = Task(description, date, latitude, longitude)
                    taskList[position] = updatedTask
                }
                TaskRepository.saveTasks(requireContext(), taskList)
                taskAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена", null)
            .setNeutralButton("Удалить") { _, _ ->
                task?.let {
                    taskList.removeAt(position)
                    TaskRepository.saveTasks(requireContext(), taskList)
                    taskAdapter.notifyDataSetChanged()
                }
            }

        builder.create().show()
    }
}

