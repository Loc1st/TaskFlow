package com.example.taskflow.ui.task

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentTaskDetailBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory

class TaskDetailFragment : Fragment(
    R.layout.fragment_task_detail
) {

    private var _binding:
            FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private var currentTask:
            com.example.taskflow.data.local.entity.TaskEntity? = null

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
            FragmentTaskDetailBinding
                .bind(view)

        val taskId =
            arguments?.getInt(
                "taskId"
            ) ?: -1

        viewModel.getTaskById(
            taskId
        ) { task ->

            if (task == null) {
                Toast.makeText(
                    requireContext(),
                    "Task not found",
                    Toast.LENGTH_SHORT
                ).show()
                return@getTaskById
            }

            currentTask = task

            binding.tvTitle.text =
                task.title

            binding.tvDescription.text =
                task.description

            binding.tvStart.text =
                "Start: ${task.startDate} ${task.startTime}"

            binding.tvEnd.text =
                "End: ${task.endDate} ${task.endTime}"

            binding.tvPriority.text =
                "Priority: ${task.priority}"

            binding.tvCategory.text =
                "Category: ${task.category}"
        }

        binding.btnComplete
            .setOnClickListener {

                currentTask?.let { task ->
                    viewModel.markTaskCompleted(task)

                    Toast.makeText(
                        requireContext(),
                        "Task completed",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController()
                        .popBackStack()
                }
            }

        binding.btnDelete
            .setOnClickListener {

                currentTask?.let { task ->
                    viewModel.deleteTask(task)

                    Toast.makeText(
                        requireContext(),
                        "Task deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController()
                        .popBackStack()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}