package com.example.taskflow.ui.reminder

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.databinding.ItemReminderBinding
import com.example.taskflow.data.firebase.model.FirebaseTask

class ReminderAdapter(

    private var tasks:
    List<FirebaseTask> = emptyList(),

    private val onClick:
        (FirebaseTask) -> Unit

) : RecyclerView.Adapter<
        ReminderAdapter.ViewHolder>() {


    class ViewHolder(

        val binding:
        ItemReminderBinding

    ) : RecyclerView.ViewHolder(
        binding.root
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(

            ItemReminderBinding.inflate(

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

        holder.binding.tvDateTime.text =

            "${task.endDate} • ${task.endTime}"



        when (

            task.priority.lowercase()

        ) {

            "high" -> {

                holder.binding.tvPriority.text =
                    "ƯU TIÊN CAO"

                setPriorityStyle(

                    holder,

                    android.R.color.holo_red_dark,

                    R.color.group_pink_light
                )
            }


            "medium" -> {

                holder.binding.tvPriority.text =
                    "ƯU TIÊN TRUNG BÌNH"

                setPriorityStyle(

                    holder,

                    R.color.status_in_progress_text,

                    R.color.status_in_progress_bg
                )
            }


            else -> {

                holder.binding.tvPriority.text =
                    "ƯU TIÊN THẤP"

                setPriorityStyle(

                    holder,

                    android.R.color.holo_green_dark,

                    R.color.status_todo_bg
                )
            }
        }



        holder.itemView
            .setOnClickListener {

                onClick(task)
            }
    }



    private fun setPriorityStyle(

        holder: ViewHolder,

        textColorRes: Int,

        backgroundRes: Int

    ) {

        val context =
            holder.itemView.context



        holder.binding.viewPriority
            .setBackgroundColor(

                ContextCompat.getColor(
                    context,
                    textColorRes
                )
            )



        holder.binding.tvPriority
            .setTextColor(

                ContextCompat.getColor(
                    context,
                    textColorRes
                )
            )



        val shape =
            GradientDrawable()

        shape.cornerRadius =
            100f

        shape.setColor(

            ContextCompat.getColor(
                context,
                backgroundRes
            )
        )

        holder.binding.tvPriority
            .background = shape
    }



    fun updateTasks(

        newTasks:
        List<FirebaseTask>

    ) {

        tasks = newTasks

        notifyDataSetChanged()
    }
}