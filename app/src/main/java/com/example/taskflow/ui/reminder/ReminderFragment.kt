package com.example.taskflow.ui.reminder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.databinding.FragmentReminderBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import java.time.LocalDate
import com.example.taskflow.data.firebase.model.FirebaseTask

class ReminderFragment : Fragment(
    R.layout.fragment_reminder
) {

    private var _binding:
            FragmentReminderBinding? = null

    private val binding
        get() = _binding!!


    private lateinit var todayAdapter:
            ReminderAdapter

    private lateinit var tomorrowAdapter:
            ReminderAdapter

    private lateinit var upcomingAdapter:
            ReminderAdapter


    private val viewModel: TaskViewModel by viewModels {
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
            FragmentReminderBinding.bind(
                view
            )


        setupRecyclerViews()





            viewModel.listenTasks { tasks ->

                if (!isAdded || _binding == null) return@listenTasks

                activity?.runOnUiThread {

                    if (!isAdded || _binding == null) return@runOnUiThread

                    loadReminderTasks(tasks)
                }
            }

        }


    private fun setupRecyclerViews() {

        todayAdapter =
            ReminderAdapter { }

        tomorrowAdapter =
            ReminderAdapter { }

        upcomingAdapter =
            ReminderAdapter { }


        binding.rvToday.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.rvTomorrow.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(
                requireContext()
            )


        binding.rvToday.adapter =
            todayAdapter

        binding.rvTomorrow.adapter =
            tomorrowAdapter

        binding.rvUpcoming.adapter =
            upcomingAdapter
    }


    private fun loadReminderTasks(
        tasks: List<FirebaseTask>
    ) {

        val today =
            LocalDate.now()

        val tomorrow =
            today.plusDays(1)


        val reminderTasks =

            tasks.filter {

                it.reminderEnabled &&
                        !it.completed
            }


        val todayTasks =
            reminderTasks.filter {

                runCatching {
                    LocalDate.parse(
                        it.endDate
                    )
                }.getOrNull() == today
            }


        val tomorrowTasks =
            reminderTasks.filter {

                runCatching {
                    LocalDate.parse(
                        it.endDate
                    )
                }.getOrNull() == tomorrow
            }


        val upcomingTasks =
            reminderTasks.filter {

                val taskDate =
                    runCatching {
                        LocalDate.parse(
                            it.endDate
                        )
                    }.getOrNull()

                taskDate != null &&
                        taskDate.isAfter(
                            tomorrow
                        )
            }


        binding.tvReminderCount.text =

            "Bạn có ${reminderTasks.size} công việc sắp đến hạn"


        todayAdapter.updateTasks(
            todayTasks
        )

        tomorrowAdapter.updateTasks(
            tomorrowTasks
        )

        upcomingAdapter.updateTasks(
            upcomingTasks
        )


        binding.tvToday.visibility =
            if (todayTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE

        binding.rvToday.visibility =
            if (todayTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE


        binding.tvTomorrow.visibility =
            if (tomorrowTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE

        binding.rvTomorrow.visibility =
            if (tomorrowTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE


        binding.tvUpcoming.visibility =
            if (upcomingTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE

        binding.rvUpcoming.visibility =
            if (upcomingTasks.isEmpty())
                View.GONE
            else
                View.VISIBLE
    }


    override fun onDestroyView() {

        viewModel.stopListening()

        super.onDestroyView()

        _binding = null

    }
}