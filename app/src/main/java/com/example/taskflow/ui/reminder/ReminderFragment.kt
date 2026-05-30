package com.example.taskflow.ui.reminder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentReminderBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.ui.home.TaskAdapter
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory

class ReminderFragment : Fragment(
    R.layout.fragment_reminder
) {

    private var _binding:
            FragmentReminderBinding? = null
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
            FragmentReminderBinding.bind(
                view
            )

        adapter =
            TaskAdapter(
                emptyList()
            ) { }

        binding.rvReminderTasks
            .layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.rvReminderTasks
            .adapter = adapter

        val userId =
            SessionManager(
                requireContext()
            ).getCurrentUserId()

        viewModel
            .getPendingTasks(userId)
            .observe(viewLifecycleOwner) {
                    tasks ->

                val reminderTasks =
                    tasks.filter {
                        it.reminderEnabled
                    }

                adapter.updateTasks(
                    reminderTasks
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}