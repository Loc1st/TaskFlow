package com.example.taskflow.ui.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import java.util.Calendar

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
        super.onViewCreated(
            view,
            savedInstanceState
        )

        _binding =
            FragmentAddTaskBinding.bind(view)

        setupDatePickers()
        setupTimePickers()

        binding.btnSaveTask.setOnClickListener {

            val title =
                binding.etTitle.text
                    .toString()
                    .trim()

            val description =
                binding.etDescription.text
                    .toString()
                    .trim()

            val startDate =
                binding.etStartDate.text
                    .toString()
                    .trim()

            val startTime =
                binding.etStartTime.text
                    .toString()
                    .trim()

            val endDate =
                binding.etEndDate.text
                    .toString()
                    .trim()

            val endTime =
                binding.etEndTime.text
                    .toString()
                    .trim()

            val category =
                binding.etCategory.text
                    .toString()
                    .trim()

            val reminder =
                binding.cbReminder.isChecked

            val priority = when (
                binding.rgPriority.checkedRadioButtonId
            ) {
                R.id.rbLow -> "Low"
                R.id.rbMedium -> "Medium"
                R.id.rbHigh -> "High"
                else -> "Low"
            }

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

            val task =
                TaskEntity(
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

                    findNavController()
                        .popBackStack()
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

    private fun setupDatePickers() {
        binding.etStartDate.setOnClickListener {
            showDatePicker(
                binding.etStartDate
            )
        }

        binding.etEndDate.setOnClickListener {
            showDatePicker(
                binding.etEndDate
            )
        }
    }

    private fun setupTimePickers() {
        binding.etStartTime.setOnClickListener {
            showTimePicker(
                binding.etStartTime
            )
        }

        binding.etEndTime.setOnClickListener {
            showTimePicker(
                binding.etEndTime
            )
        }
    }

    private fun showDatePicker(
        target: EditText
    ) {
        val calendar =
            Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date =
                    String.format(
                        "%04d-%02d-%02d",
                        year,
                        month + 1,
                        day
                    )

                target.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(
        target: EditText
    ) {
        val calendar =
            Calendar.getInstance()

        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val time =
                    String.format(
                        "%02d:%02d",
                        hour,
                        minute
                    )

                target.setText(time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}