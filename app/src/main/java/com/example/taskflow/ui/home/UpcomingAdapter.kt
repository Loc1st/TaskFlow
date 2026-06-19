package com.example.taskflow.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.data.firebase.model.FirebaseTask
import com.example.taskflow.databinding.ItemUpcomingTaskBinding

class UpcomingAdapter(

    private var tasks: List<FirebaseTask> = emptyList(),

    private val onClick: (FirebaseTask) -> Unit

) : RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemUpcomingTaskBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(

            ItemUpcomingTaskBinding.inflate(

                LayoutInflater.from(parent.context),

                parent,

                false

            )

        )

    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(

        holder: ViewHolder,

        position: Int

    ) {

        val task = tasks[position]

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

                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_high
                )

            }

            "medium" -> {

                holder.binding.tvPriority.text = "TRUNG BÌNH"

                holder.binding.tvPriority.setTextColor(

                    holder.itemView.context.getColor(

                        R.color.status_in_progress_text

                    )

                )

                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_medium
                )

            }

            else -> {

                holder.binding.tvPriority.text = "THẤP"

                holder.binding.tvPriority.setTextColor(

                    holder.itemView.context.getColor(

                        R.color.status_todo_text

                    )

                )

                holder.binding.root.setBackgroundResource(
                    R.drawable.bg_task_low
                )

            }

        }

        holder.binding.tvDeadline.text =
            "⏰ Kết thúc: ${task.endTime}"

        holder.binding.pbProgress.progress =

            if (task.completed)

                100

            else

                50

        holder.itemView.setOnClickListener {

            onClick(task)

        }

    }

    fun update(

        newTasks: List<FirebaseTask>

    ) {

        tasks = newTasks

        notifyDataSetChanged()

    }

}