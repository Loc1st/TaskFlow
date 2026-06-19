package com.example.taskflow.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.databinding.FragmentCalendarBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import com.example.taskflow.data.firebase.model.FirebaseTask
class CalendarFragment : Fragment(
    R.layout.fragment_calendar
) {

    private var _binding:
            FragmentCalendarBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var adapter:
            CalendarTaskAdapter

    private lateinit var dateAdapter:
            DateAdapter

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory()
    }

    private var allTasks = emptyList<FirebaseTask>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        _binding =
            FragmentCalendarBinding.bind(
                view
            )

        adapter =
            CalendarTaskAdapter(
                emptyList()
            ) { task ->

                val bundle =
                    Bundle()

                bundle.putString(
                    "taskId",
                    task.id
                )

                findNavController()
                    .navigate(
                        R.id.action_calendarFragment_to_taskDetailFragment,
                        bundle
                    )
            }

        binding.rvCalendarTasks
            .layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.rvCalendarTasks
            .adapter =
            adapter



        val dateList =
            mutableListOf<DateModel>()

        val todayDate =
            LocalDate.now()

        for (i in -30..30) {

            val date =
                todayDate.plusDays(
                    i.toLong()
                )

            dateList.add(

                DateModel(
                    date =
                        date.toString(),

                    dayNumber =
                        date.dayOfMonth
                            .toString(),

                    dayName =
                        when (date.dayOfWeek.value) {

                            1 -> "T2"
                            2 -> "T3"
                            3 -> "T4"
                            4 -> "T5"
                            5 -> "T6"
                            6 -> "T7"
                            7 -> "CN"

                            else -> ""
                        },

                    isSelected =
                        i == 0
                )
            )
        }

        dateAdapter =
            DateAdapter(dateList) { selectedDate ->

                viewModel.listenTasks { tasks ->

                    if (!isAdded || _binding == null) return@listenTasks

                    allTasks = tasks.filter {

                        it.startDate == selectedDate.date

                    }

                    adapter.updateTasks(allTasks)

                }

            }

        val layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

        binding.rvDates.layoutManager =
            layoutManager

        binding.rvDates.adapter =
            dateAdapter

        binding.rvDates.post {

            layoutManager
                .scrollToPositionWithOffset(
                    30,
                    binding.rvDates.width / 2 - 36
                )
        }

        val today =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )
                .format(
                    Date()
                )

        viewModel.listenTasks { tasks ->

            if (!isAdded || _binding == null) return@listenTasks

            allTasks =
                tasks.filter {

                    it.startDate == today

                }

            adapter.updateTasks(allTasks)

        }



        binding.btnAll.setOnClickListener {

            selectFilter(
                binding.btnAll
            )

            adapter.updateTasks(
                allTasks
            )
        }



        binding.btnTodo.setOnClickListener {

            selectFilter(
                binding.btnTodo
            )

            adapter.updateTasks(

                allTasks.filter {

                    !it.completed
                }
            )
        }



        binding.btnDone.setOnClickListener {

            selectFilter(
                binding.btnDone
            )

            adapter.updateTasks(

                allTasks.filter {

                    it.completed
                }
            )
        }

    }

    private fun selectFilter(
        selected: View
    ) {

        val buttons = listOf(
            binding.btnAll,
            binding.btnTodo,
            binding.btnDone
        )

        buttons.forEach {

            it.setBackgroundResource(
                R.drawable.bg_filter_normal
            )

            it.setTextColor(
                requireContext().getColor(
                    R.color.primary_color
                )
            )
        }

        selected.setBackgroundResource(
            R.drawable.bg_filter_selected
        )

        when(selected) {

            binding.btnAll ->
                binding.btnAll.setTextColor(
                    requireContext().getColor(
                        R.color.white
                    )
                )

            binding.btnTodo ->
                binding.btnTodo.setTextColor(
                    requireContext().getColor(
                        R.color.white
                    )
                )

            binding.btnDone ->
                binding.btnDone.setTextColor(
                    requireContext().getColor(
                        R.color.white
                    )
                )
        }
    }

    override fun onDestroyView() {

        viewModel.stopListening()

        super.onDestroyView()

        _binding = null

    }

}
