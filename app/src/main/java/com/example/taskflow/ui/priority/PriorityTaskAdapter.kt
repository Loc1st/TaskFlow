package com.example.taskflow.ui.priority

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.databinding.ItemPriorityTaskBinding
import com.example.taskflow.R
import com.example.taskflow.data.firebase.model.FirebaseTask
class PriorityTaskAdapter(

    private var tasks: List<FirebaseTask> =
        emptyList(),

    private val onClick:
        (FirebaseTask) -> Unit

) : RecyclerView.Adapter<
        PriorityTaskAdapter.ViewHolder>() {

    class ViewHolder(
        val binding:
        ItemPriorityTaskBinding
    ) :
        RecyclerView.ViewHolder(
            binding.root
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(

            ItemPriorityTaskBinding.inflate(

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

        holder.binding.tvDate.text =
            task.endDate

        holder.binding.tvDescription.text =
            task.description

        holder.itemView
            .setOnClickListener {

                onClick(task)
            }

        holder.binding.tvTitle.text =
            task.title

        holder.binding.tvDescription.text =
            task.description

        holder.binding.tvDate.text =
            task.endDate

        if (task.completed) {

            holder.binding.tvStatus.text =
                "ĐÃ HOÀN THÀNH"

            holder.binding.tvStatus.setTextColor(
                holder.itemView.context.getColor(
                    R.color.status_done_text
                )
            )

            holder.binding.tvStatus.setBackgroundResource(
                R.drawable.bg_status_done
            )

        } else {

            holder.binding.tvStatus.text =
                "CHƯA HOÀN THÀNH"

            holder.binding.tvStatus.setTextColor(
                holder.itemView.context.getColor(
                    R.color.status_todo_text
                )
            )

            holder.binding.tvStatus.setBackgroundResource(
                R.drawable.bg_status_todo
            )
        }
    }

    fun update(
        newTasks: List<FirebaseTask>
    ) {

        tasks = newTasks

        notifyDataSetChanged()
    }
}