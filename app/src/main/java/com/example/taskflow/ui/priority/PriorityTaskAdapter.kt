package com.example.taskflow.ui.priority

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.databinding.ItemUpcomingTaskBinding

class PriorityTaskAdapter(

    private var tasks: List<TaskEntity> =
        emptyList(),

    private val onClick:
        (TaskEntity) -> Unit

) : RecyclerView.Adapter<
        PriorityTaskAdapter.ViewHolder>() {

    class ViewHolder(
        val binding:
        ItemUpcomingTaskBinding
    ) :
        RecyclerView.ViewHolder(
            binding.root
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(

            ItemUpcomingTaskBinding.inflate(

                LayoutInflater.from(
                    parent.context
                ),

                parent,

                false
            )
        )
    }

    override fun getItemCount() =
        tasks.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val task =
            tasks[position]

        holder.binding.tvTitle.text =
            task.title

        holder.binding.tvPriority.text =
            task.priority

        holder.binding.tvDeadline.text =
            task.endDate

        holder.itemView
            .setOnClickListener {

                onClick(task)
            }
    }

    fun update(
        newTasks: List<TaskEntity>
    ) {

        tasks = newTasks

        notifyDataSetChanged()
    }
}