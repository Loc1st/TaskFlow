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

        holder.binding.tvPriority.text =
            task.priority.uppercase()

        holder.binding.tvDeadline.text =
            task.endTime



        when (
            task.priority.lowercase()
        ) {

            "low" -> {

                holder.binding.root
                    .setBackgroundColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            R.color.status_todo_bg
                        )
                    )

                holder.binding.tvPriority
                    .setTextColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            R.color.status_todo_text
                        )
                    )
            }



            "medium" -> {

                holder.binding.root
                    .setBackgroundColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            R.color.status_in_progress_bg
                        )
                    )

                holder.binding.tvPriority
                    .setTextColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            R.color.status_in_progress_text
                        )
                    )
            }



            else -> {

                holder.binding.root
                    .setBackgroundColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            R.color.group_pink_light
                        )
                    )

                holder.binding.tvPriority
                    .setTextColor(

                        ContextCompat.getColor(

                            holder.itemView.context,

                            android.R.color.holo_red_dark
                        )
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