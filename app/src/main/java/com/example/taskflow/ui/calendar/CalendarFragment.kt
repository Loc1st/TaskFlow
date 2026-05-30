package com.example.taskflow.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentCalendarBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(
    R.layout.fragment_calendar
) {

    private var _binding:
            FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter:
            CalendarTaskAdapter

    private lateinit var dateAdapter:
            DateAdapter

    private val viewModel:
            TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(
                DatabaseProvider
                    .getDatabase(
                        requireContext()
                    )
                    .taskDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        _binding =
            FragmentCalendarBinding.bind(view)

        adapter =
            CalendarTaskAdapter(
                emptyList()
            )

        binding.rvCalendarTasks
            .layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.rvCalendarTasks
            .adapter = adapter

        val userId =
            SessionManager(
                requireContext()
            ).getCurrentUserId()

        // RecyclerView ngày ngang
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
                    date = date.toString(),
                    dayNumber =
                        date.dayOfMonth.toString(),
                    dayName =
                        date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                    isSelected = (i == 0)
                )
            )
        }

        dateAdapter =
            DateAdapter(
                dateList
            ) { selectedDate ->

                viewModel
                    .getTasksByDate(
                        userId,
                        selectedDate.date
                    )
                    .observe(
                        viewLifecycleOwner
                    ) { tasks ->

                        adapter.updateTasks(
                            tasks
                        )
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

            layoutManager.scrollToPositionWithOffset(
                30,
                binding.rvDates.width / 2 - 36
            )
        }

        // Load hôm nay mặc định
        val today =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

        viewModel
            .getTasksByDate(
                userId,
                today
            )
            .observe(
                viewLifecycleOwner
            ) { tasks ->

                adapter.updateTasks(
                    tasks
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}