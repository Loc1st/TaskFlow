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
import com.example.taskflow.data.firebase.model.FirebaseTask
import com.example.taskflow.databinding.FragmentAddTaskBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import java.util.Calendar
import com.example.taskflow.notification.DateTimeUtil
import com.example.taskflow.notification.ReminderScheduler
class AddTaskFragment : Fragment(
    R.layout.fragment_add_task
) {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddTaskBinding.bind(view)

        setupDatePickers()

        setupTimePickers()

        binding.btnSaveTask.setOnClickListener {

            saveTask()

        }

    }

    private fun saveTask() {

        val title =
            binding.etTitle.text.toString().trim()

        if (title.isEmpty()) {

            Toast.makeText(
                requireContext(),
                "Nhập tiêu đề",
                Toast.LENGTH_SHORT
            ).show()

            return

        }

        val priority = when (
            binding.rgPriority.checkedRadioButtonId
        ) {

            R.id.rbHigh -> "High"

            R.id.rbMedium -> "Medium"

            else -> "Low"

        }

        val task = FirebaseTask(

            title = title,

            description =
                binding.etDescription.text.toString(),

            startDate =
                binding.etStartDate.text.toString(),

            startTime =
                binding.etStartTime.text.toString(),

            endDate =
                binding.etEndDate.text.toString(),

            endTime =
                binding.etEndTime.text.toString(),

            category =
                binding.etCategory.text.toString(),

            priority = priority,

            reminderEnabled =
                binding.cbReminder.isChecked,

            completed = false

        )

        viewModel.addTask(task) { success, savedTask ->

            if (success && savedTask != null) {

                if (savedTask.reminderEnabled) {

                    val triggerTime =

                        DateTimeUtil.toMillis(

                            savedTask.endDate,

                            savedTask.endTime

                        )

                    if (triggerTime > System.currentTimeMillis()) {

                        ReminderScheduler(requireContext())
                            .scheduleReminder(

                                taskId = savedTask.id,

                                title = savedTask.title,

                                description = savedTask.description,

                                triggerTimeMillis = triggerTime

                            )
                            android.util.Log.d(
                                "REMINDER",
                                "Schedule success ${savedTask.title}"
                            )

                    }

                }

                Toast.makeText(

                    requireContext(),

                    "Đã thêm công việc",

                    Toast.LENGTH_SHORT

                ).show()

                findNavController().popBackStack()

            } else {

                Toast.makeText(

                    requireContext(),

                    "Lưu thất bại",

                    Toast.LENGTH_SHORT

                ).show()

            }

        }

    }

    private fun setupDatePickers() {

        binding.etStartDate.setOnClickListener {

            showDatePicker(binding.etStartDate)

        }

        binding.etEndDate.setOnClickListener {

            showDatePicker(binding.etEndDate)

        }

    }

    private fun setupTimePickers() {

        binding.etStartTime.setOnClickListener {

            showTimePicker(binding.etStartTime)

        }

        binding.etEndTime.setOnClickListener {

            showTimePicker(binding.etEndTime)

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

                target.setText(

                    String.format(

                        "%04d-%02d-%02d",

                        year,

                        month + 1,

                        day

                    )

                )

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

                target.setText(

                    String.format(

                        "%02d:%02d",

                        hour,

                        minute

                    )

                )

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