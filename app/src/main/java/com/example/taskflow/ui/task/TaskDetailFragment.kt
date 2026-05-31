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
import com.example.taskflow.databinding.FragmentTaskDetailBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory

class TaskDetailFragment :
    Fragment(
        R.layout.fragment_task_detail
    ) {

    private var _binding:
            FragmentTaskDetailBinding? =
        null

    private val binding
        get() = _binding!!

    private var currentTask:
            TaskEntity? = null

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

        binding.btnBack
            .setOnClickListener {

                findNavController()
                    .navigateUp()
            }

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

            currentTask =
                task

            binding.tvTitle.text =
                task.title

            binding.tvDescription.text =
                task.description

            binding.tvStart.text =
                " Start\n${task.startDate} :  ${task.startTime}"

            binding.tvEnd.text =
                " End\n${task.endDate} :  ${task.endTime}"

            binding.tvCategory.text =
                " Category\n${task.category}"

            when (
                task.priority.lowercase()
            ) {

                "low" -> {

                    binding.tvPriority.text =
                        "LOW"

                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_low
                    )

                    binding.tvPriority.setTextColor(
                        android.graphics.Color.parseColor(
                            "#2EB872"
                        )
                    )
                }

                "medium" -> {

                    binding.tvPriority.text =
                        "MEDIUM"

                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_medium
                    )

                    binding.tvPriority.setTextColor(
                        requireContext().getColor(
                            R.color.group_orange
                        )
                    )
                }

                else -> {

                    binding.tvPriority.text =
                        "HIGH"

                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_high
                    )

                    binding.tvPriority.setTextColor(
                        android.graphics.Color.parseColor(
                            "#FF4D4D"
                        )
                    )
                }
            }

            updateButton(
                task
            )
        }


        binding.btnComplete
            .setOnClickListener {

                currentTask?.let {

                    val updated =

                        it.copy(

                            isCompleted =
                                !it.isCompleted,

                            syncPending =
                                true
                        )

                    viewModel.updateTask(
                        updated
                    )

                    Toast.makeText(

                        requireContext(),

                        if (
                            updated.isCompleted
                        )
                            "Task completed"
                        else
                            "Task moved to To-do",

                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController()
                        .popBackStack()
                }
            }


        binding.btnDelete
            .setOnClickListener {

                currentTask?.let {

                    viewModel.deleteTask(
                        it
                    )

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

    private fun updateButton(
        task: TaskEntity
    ) {

        binding.btnComplete.text =

            if (
                task.isCompleted
            ) {
                "↩ Mark as To-do"
            } else {
                "✓ Mark as Done"
            }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding =
            null
    }
}