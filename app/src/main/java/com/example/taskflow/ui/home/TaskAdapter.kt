package com.example.taskflow.ui.home

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.data.local.entity.TaskEntity

class TaskAdapter(
    private var taskList: List<TaskEntity>,
    private val onClick: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(
        val textView: TextView
    ) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val textView = TextView(
            parent.context
        )

        textView.textSize = 18f
        textView.setPadding(
            24, 24, 24, 24
        )

        return TaskViewHolder(
            textView
        )
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task = taskList[position]

        holder.textView.text =
            task.title

        holder.itemView
            .setOnClickListener {
                onClick(task)
            }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateTasks(
        newTasks: List<TaskEntity>
    ) {
        taskList = newTasks
        notifyDataSetChanged()
    }
}