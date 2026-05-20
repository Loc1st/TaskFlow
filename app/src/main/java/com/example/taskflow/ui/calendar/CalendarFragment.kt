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
import com.example.taskflow.ui.home.TaskAdapter
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(
    R.layout.fragment_calendar
) {

    private var _binding:
            FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter:
            TaskAdapter

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
            TaskAdapter(
                emptyList()
            ) { }

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

        binding.calendarView
            .setOnDateChangeListener {
                    _, year, month, dayOfMonth ->

                val selectedDate =
                    String.format(
                        "%04d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )

                viewModel
                    .getTasksByDate(
                        userId,
                        selectedDate
                    )
                    .observe(
                        viewLifecycleOwner
                    ) { tasks ->
                        adapter.updateTasks(
                            tasks
                        )
                    }
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