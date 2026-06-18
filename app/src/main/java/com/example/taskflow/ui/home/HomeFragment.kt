package com.example.taskflow.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.databinding.FragmentHomeBinding
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.fragment.findNavController
import com.example.taskflow.viewmodel.UserViewModel
import androidx.fragment.app.activityViewModels
class HomeFragment : Fragment(
    R.layout.fragment_home
) {



    private var _binding:
            FragmentHomeBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var upcomingAdapter:
            UpcomingAdapter

    private val userViewModel: UserViewModel by activityViewModels()
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
            FragmentHomeBinding.bind(
                view
            )
        userViewModel.loadCurrentUser()

        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->

            if (_binding == null) return@observe

            binding.tvUserName.text =
                user?.username ?: ""

        }


        setupUpcoming()

        val today =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(
                Date()
            )

        viewModel.listenTasks { tasks ->

            if (!isAdded || _binding == null) return@listenTasks

            activity?.runOnUiThread {

                if (_binding == null) return@runOnUiThread

                val today = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

                val todayTasks =
                    tasks.filter {

                        it.startDate == today

                    }

                val total =
                    todayTasks.size

                val done =
                    todayTasks.count {

                        it.completed

                    }

                binding.tvTodayProgress.text =
                    "$done / $total completed"

                val percent =

                    if (total == 0)

                        0

                    else

                        done * 100 / total

                binding.tvPercent.text =
                    "$percent%"

                binding.circleProgress.progress =
                    percent

                upcomingAdapter.update(

                    todayTasks

                        .sortedBy {

                            it.endTime

                        }

                        .take(3)

                )

                val highTasks = tasks.filter {
                    it.priority.equals("High", true)
                }

                val mediumTasks = tasks.filter {
                    it.priority.equals("Medium", true)
                }

                val lowTasks = tasks.filter {
                    it.priority.equals("Low", true)
                }

                val highPercent =
                    if (highTasks.isEmpty())
                        0
                    else
                        highTasks.count { it.completed } * 100 / highTasks.size

                val mediumPercent =
                    if (mediumTasks.isEmpty())
                        0
                    else
                        mediumTasks.count { it.completed } * 100 / mediumTasks.size

                val lowPercent =
                    if (lowTasks.isEmpty())
                        0
                    else
                        lowTasks.count { it.completed } * 100 / lowTasks.size

                bindPriorityCard(
                    binding.cardHigh.root,
                    "CAO",
                    highTasks.size,
                    highPercent
                )

                bindPriorityCard(
                    binding.cardMedium.root,
                    "TRUNG BÌNH",
                    mediumTasks.size,
                    mediumPercent
                )

                bindPriorityCard(
                    binding.cardLow.root,
                    "THẤP",
                    lowTasks.size,
                    lowPercent
                )

            }

        }

        binding.btnViewCalendar
            .setOnClickListener {

                findNavController()
                    .navigate(
                        R.id.calendarFragment
                    )
            }

        binding.cardHigh.root
            .setOnClickListener {

                val bundle =
                    Bundle()

                bundle.putString(
                    "priority",
                    "high"
                )

                findNavController()
                    .navigate(
                        R.id.priorityTasksFragment,
                        bundle
                    )
            }


        binding.cardMedium.root
            .setOnClickListener {

                val bundle =
                    Bundle()

                bundle.putString(
                    "priority",
                    "medium"
                )

                findNavController()
                    .navigate(
                        R.id.priorityTasksFragment,
                        bundle
                    )
            }


        binding.cardLow.root
            .setOnClickListener {

                val bundle =
                    Bundle()

                bundle.putString(
                    "priority",
                    "low"
                )

                findNavController()
                    .navigate(
                        R.id.priorityTasksFragment,
                        bundle
                    )
            }
    }



    private fun setupUpcoming() {

        upcomingAdapter =

            UpcomingAdapter {

                    task ->

                val bundle =
                    Bundle()

                bundle.putString(
                    "taskId",
                    task.id
                )

                findNavController()
                    .navigate(
                        R.id.taskDetailFragment,
                        bundle
                    )
            }

        binding.rvUpcoming.layoutManager =

            LinearLayoutManager(

                requireContext(),

                LinearLayoutManager.HORIZONTAL,

                false
            )

        binding.rvUpcoming.adapter =
            upcomingAdapter
    }




    private fun bindPriorityCard(

        card:
        View,

        title:
        String,

        count:
        Int,

        percent:
        Int

    ) {


        val tvPriority =
            card.findViewById<TextView>(
                R.id.tvPriority
            )

        val tvCount =
            card.findViewById<TextView>(
                R.id.tvCount
            )

        val tvPercent =
            card.findViewById<TextView>(
                R.id.tvPercent
            )

        val circle =
            card.findViewById<
                    CircularProgressIndicator>(
                R.id.circle
            )



        tvPriority.text =
            title

        tvCount.text =
            "$count Tasks"

        tvPercent.text =
            "$percent%"

        circle.progress =
            percent



        when (
            title
        ) {

            "THẤP" -> {

                tvPriority.setTextColor(
                    requireContext().getColor(
                        android.R.color.holo_green_light
                    )
                )

                circle.setIndicatorColor(
                    requireContext().getColor(
                        android.R.color.holo_green_light
                    )
                )
            }



            "TRUNG BÌNH" -> {

                tvPriority.setTextColor(
                    requireContext().getColor(
                        R.color.status_in_progress_text
                    )
                )

                circle.setIndicatorColor(
                    requireContext().getColor(
                        R.color.status_in_progress_text
                    )
                )
            }



            "CAO" -> {

                tvPriority.setTextColor(
                    requireContext().getColor(
                        android.R.color.holo_red_dark
                    )
                )

                circle.setIndicatorColor(
                    requireContext().getColor(
                        android.R.color.holo_red_dark
                    )
                )
            }
        }
    }





    override fun onDestroyView() {

        viewModel.stopListening()

        super.onDestroyView()

        _binding = null
    }
}