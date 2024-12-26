package ru.vsu.vladimir.vsu_lr2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TaskAdapter(context: Context, private val tasks: List<Task>) :
    ArrayAdapter<Task>(context, R.layout.list_item_task, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false)

        val task = tasks[position]

        // Заполняем данные в элементы списка
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        descriptionTextView.text = task.description
        dateTextView.text = task.date

        return view
    }
}
