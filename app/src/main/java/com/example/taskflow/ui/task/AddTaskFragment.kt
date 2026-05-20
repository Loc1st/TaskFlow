package com.example.taskflow.ui.task

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentAddTaskBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory

class AddTaskFragment : Fragment(
    R.layout.fragment_add_task
) {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(
                DatabaseProvider
                    .getDatabase(requireContext())
                    .taskDao()
            )
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddTaskBinding.bind(view)

        binding.btnSaveTask.setOnClickListener {

            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val startDate = binding.etStartDate.text.toString().trim()
            val startTime = binding.etStartTime.text.toString().trim()
            val endDate = binding.etEndDate.text.toString().trim()
            val endTime = binding.etEndTime.text.toString().trim()
            val priority = binding.etPriority.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val reminder = binding.cbReminder.isChecked

            if (title.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Title required",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val userId =
                SessionManager(
                    requireContext()
                ).getCurrentUserId()

            val task = TaskEntity(
                userId = userId,
                title = title,
                description = description,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
                priority = priority,
                category = category,
                reminderEnabled = reminder,
                isCompleted = false,
                syncPending = true
            )

            viewModel.addTask(task) { id ->
                if (id > 0) {
                    Toast.makeText(
                        requireContext(),
                        "Task added",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Save failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}