package com.example.taskflow.ui.task

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.databinding.FragmentTaskDetailBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import com.example.taskflow.data.firebase.model.FirebaseTask
import com.example.taskflow.notification.ReminderScheduler

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
            FirebaseTask? = null

    private val viewModel:
            TaskViewModel by viewModels {

        TaskViewModelFactory()

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
            arguments?.getString(
                "taskId"
            ) ?: ""

        binding.btnBack
            .setOnClickListener {

                findNavController()
                    .navigateUp()
            }

        viewModel.listenTasks { tasks ->

            if (!isAdded || _binding == null) return@listenTasks

            val task = tasks.find {

                it.id == taskId

            } ?: return@listenTasks

            currentTask = task

            binding.tvTitle.text = task.title

            binding.tvDescription.text = task.description

            binding.tvStart.text =
                "📅 Bắt đầu\n${task.startDate} • ${task.startTime}"

            binding.tvEnd.text =
                "⏰ Kết thúc\n${task.endDate} • ${task.endTime}"

            binding.tvCategory.text =
                "📂 Danh mục\n${task.category}"

            when (task.priority.lowercase()) {

                "high" -> {

                    binding.tvPriority.text = "CAO"
                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_high
                    )

                }

                "medium" -> {

                    binding.tvPriority.text = "TRUNG BÌNH"
                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_medium
                    )

                }

                else -> {

                    binding.tvPriority.text = "THẤP"
                    binding.tvPriority.setBackgroundResource(
                        R.drawable.bg_priority_low
                    )

                }

            }

            updateButton(task)

        }


        binding.btnComplete.setOnClickListener {

            currentTask?.let { task ->

                if (!task.completed) {

                    ReminderScheduler(requireContext())
                        .cancelReminder(task.id)

                }

                viewModel.updateTask(

                    task.copy(

                        completed = !task.completed

                    )

                ) { success ->

                    if (success) {

                        Toast.makeText(
                            requireContext(),
                            "Đã cập nhật",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }

        }


        binding.btnDelete.setOnClickListener {

            currentTask?.let { task ->

                // Hủy Alarm trước khi xóa Task
                ReminderScheduler(requireContext())
                    .cancelReminder(task.id)

                viewModel.deleteTask(
                    task.id
                ) { success ->

                    if (success) {

                        Toast.makeText(
                            requireContext(),
                            "Đã xóa",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().popBackStack()

                    } else {

                        Toast.makeText(
                            requireContext(),
                            "Xóa thất bại",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }

        }
    }

    private fun updateButton(
        task: FirebaseTask
    ) {

        binding.btnComplete.text =

            if (
                task.completed
            ) {
                "Chưa hoàn thành"
            } else {
                "Hoàn thành"
            }
    }

    override fun onDestroyView() {

        viewModel.stopListening()

        super.onDestroyView()

        _binding = null
    }
}