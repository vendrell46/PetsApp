package com.carlos.myapps.petsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val taskName: TextView = view.findViewById(R.id.task_name)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
    }

    var checkListener: ((Task, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_task, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.name
        holder.checkBox.isChecked = task.complete
        holder.checkBox.setOnCheckedChangeListener { _, checked ->
            checkListener?.invoke(task, checked)
        }
    }
}