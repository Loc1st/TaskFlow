package com.example.taskflow.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import java.time.LocalDate

class DateAdapter(
    private val dates: MutableList<DateModel>,
    private val onDateClick: (DateModel) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvMonth: TextView =
            itemView.findViewById(R.id.tvMonth)

        val tvDayNumber: TextView =
            itemView.findViewById(R.id.tvDayNumber)

        val tvDayName: TextView =
            itemView.findViewById(R.id.tvDayName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_calendar_date,
                    parent,
                    false
                )

        return DateViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DateViewHolder,
        position: Int
    ) {

        val item = dates[position]

        val date =
            LocalDate.parse(
                item.date
            )

        val month =
            "Th${date.monthValue}"

        holder.tvMonth.text = month

        holder.tvDayNumber.text =
            item.dayNumber

        holder.tvDayName.text =
            item.dayName

        val isToday =
            item.date ==
                    LocalDate.now().toString()

        if (item.isSelected) {

            // Ngày đang được chọn
            holder.itemView.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_date_selected
                )

            holder.tvMonth.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )

            holder.tvDayNumber.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )

            holder.tvDayName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )

            holder.tvDayNumber.textSize = 24f

        } else if (isToday) {

            // Hôm nay nhưng không được chọn
            holder.itemView.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_date_today
                )

            holder.tvMonth.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primary_color
                )
            )

            holder.tvDayNumber.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primary_color
                )
            )

            holder.tvDayName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primary_color
                )
            )

            holder.tvDayNumber.textSize = 24f

        } else {

            // Ngày bình thường
            holder.itemView.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_date_normal
                )

            holder.tvMonth.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.text_secondary
                )
            )

            holder.tvDayNumber.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.text_primary
                )
            )

            holder.tvDayName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.text_secondary
                )
            )

            holder.tvDayNumber.textSize = 22f
        }

        holder.itemView.setOnClickListener {

            dates.forEach {
                it.isSelected = false
            }

            item.isSelected = true

            notifyDataSetChanged()

            onDateClick(item)
        }
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}