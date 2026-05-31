package com.example.taskflow.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.data.local.entity.TaskEntity

class CalendarTaskAdapter(
    private var taskList: List<TaskEntity>,
    private val onClick: (TaskEntity) -> Unit
) : RecyclerView.Adapter<CalendarTaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvCategory: TextView =
            itemView.findViewById(R.id.tvCategory)

        val tvTitle: TextView =
            itemView.findViewById(R.id.tvTitle)

        val tvTime: TextView =
            itemView.findViewById(R.id.tvTime)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_calendar_task,
                    parent,
                    false
                )

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = taskList[position]

        // PRIORITY
        holder.tvCategory.text =
            task.priority.uppercase()

        when (task.priority.lowercase()) {

            "low" -> {
                holder.tvCategory.setTextColor(
                    holder.itemView.context.getColor(
                        android.R.color.holo_green_dark
                    )
                )
            }

            "medium" -> {
                holder.tvCategory.setTextColor(
                    holder.itemView.context.getColor(
                        R.color.group_orange
                    )
                )
            }

            "high" -> {
                holder.tvCategory.setTextColor(
                    holder.itemView.context.getColor(
                        android.R.color.holo_red_dark
                    )
                )
            }
        }

        holder.tvTitle.text =
            task.title

        holder.tvTime.text =
            task.startTime

        // STATUS
        if (task.isCompleted) {

            holder.tvStatus.text =
                "Done"

            holder.tvStatus.setTextColor(
                holder.itemView.context.getColor(
                    R.color.status_done_text
                )
            )

            holder.tvStatus.setBackgroundResource(
                R.drawable.bg_status_done
            )

        } else {

            holder.tvStatus.text =
                "To-do"

            holder.tvStatus.setTextColor(
                holder.itemView.context.getColor(
                    R.color.status_todo_text
                )
            )

            holder.tvStatus.setBackgroundResource(
                R.drawable.bg_status_todo
            )
        }

        // CLICK CARD
        holder.itemView.setOnClickListener {

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
