package com.example.taskflow.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.databinding.ItemUpcomingTaskBinding

class UpcomingAdapter(

    private var tasks:
    List<TaskEntity> =
        emptyList(),

    private val onClick:
        (TaskEntity) -> Unit

) : RecyclerView.Adapter<
        UpcomingAdapter.ViewHolder>() {


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

        when (task.priority.lowercase()) {

            "high" -> {
                holder.binding.tvPriority.text = "CAO"
                holder.binding.tvPriority.setTextColor(
                    holder.itemView.context.getColor(
                        android.R.color.holo_red_dark
                    )
                )
            }

            "medium" -> {
                holder.binding.tvPriority.text = "TRUNG BÌNH"
                holder.binding.tvPriority.setTextColor(
                    holder.itemView.context.getColor(
                        R.color.status_in_progress_text
                    )
                )
            }

            "low" -> {
                holder.binding.tvPriority.text = "THẤP"
                holder.binding.tvPriority.setTextColor(
                    holder.itemView.context.getColor(
                        R.color.status_todo_text
                    )
                )
            }
        }

        holder.binding.tvDeadline.text =
            "⏰ Kết thúc: ${task.endTime}"



        when(task.priority.lowercase()) {

            "high" -> {
                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_high
                )
            }

            "medium" -> {
                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_medium
                )
            }

            "low" -> {
                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_low
                )
            }
        }



        holder.binding.pbProgress.progress =

            if (
                task.isCompleted
            )
                100
            else
                50

        holder.itemView.setOnClickListener {

            onClick(
                task
            )
        }
    }





    fun update(
        newTasks:
        List<TaskEntity>
    ) {

        tasks =
            newTasks

        notifyDataSetChanged()
    }



}