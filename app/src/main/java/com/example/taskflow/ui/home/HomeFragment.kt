package com.example.taskflow.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentHomeBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment(
    R.layout.fragment_home
) {

    private var _binding:
            FragmentHomeBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var upcomingAdapter:
            UpcomingAdapter

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
            FragmentHomeBinding.bind(
                view
            )

        val userId =
            SessionManager(
                requireContext()
            )
                .getCurrentUserId()

        loadUserInfo(
            userId
        )

        setupUpcoming()

        val today =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(
                Date()
            )

        viewModel
            .getTasksByDate(
                userId,
                today
            )
            .observe(
                viewLifecycleOwner
            ) { tasks ->

                val total =
                    tasks.size

                val done =
                    tasks.count {
                        it.isCompleted
                    }

                binding.tvTodayProgress.text =
                    "$done / $total completed"

                val percent =

                    if (
                        total == 0
                    )
                        0
                    else
                        done * 100 / total

                binding.tvPercent.text =
                    "$percent%"

                binding.circleProgress.progress =
                    percent



                upcomingAdapter.update(

                    tasks
                        .sortedBy {
                            it.endTime
                        }
                        .take(3)
                )






            }
        viewModel
            .getTasksByUser(
                userId
            )
            .observe(
                viewLifecycleOwner
            ) { tasks ->

                val highTasks =
                    tasks.filter {
                        it.priority.trim()
                            .equals(
                                "high",
                                true
                            )
                    }

                val highCompleted =
                    highTasks.count {
                        it.isCompleted
                    }

                bindPriorityCard(
                    binding.cardHigh.root,
                    "CAO",
                    highTasks.size,

                    if (highTasks.isEmpty())
                        0
                    else
                        highCompleted * 100 /
                                highTasks.size
                )



                val mediumTasks =
                    tasks.filter {
                        it.priority.trim()
                            .equals(
                                "medium",
                                true
                            )
                    }

                val mediumCompleted =
                    mediumTasks.count {
                        it.isCompleted
                    }

                bindPriorityCard(
                    binding.cardMedium.root,
                    "TRUNG BÌNH",
                    mediumTasks.size,

                    if (mediumTasks.isEmpty())
                        0
                    else
                        mediumCompleted * 100 /
                                mediumTasks.size
                )



                val lowTasks =
                    tasks.filter {
                        it.priority.trim()
                            .equals(
                                "low",
                                true
                            )
                    }

                val lowCompleted =
                    lowTasks.count {
                        it.isCompleted
                    }

                bindPriorityCard(
                    binding.cardLow.root,
                    "THẤP",
                    lowTasks.size,

                    if (lowTasks.isEmpty())
                        0
                    else
                        lowCompleted * 100 /
                                lowTasks.size
                )
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

                bundle.putInt(
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


    private fun loadUserInfo(
        userId: Int
    ) {

        viewLifecycleOwner.lifecycleScope.launch {

            val user =

                DatabaseProvider
                    .getDatabase(
                        requireContext()
                    )
                    .userDao()
                    .getUserById(
                        userId
                    )

            user?.let {

                binding.tvUserName.text =
                    it.username

                /*
                 * Sau này thêm Avatar:
                 *
                 * Glide.with(this@HomeFragment)
                 *     .load(it.avatarUrl)
                 *     .into(binding.imgAvatar)
                 */
                //đã thêm phần avatar rồi
                it.avatarPath?.let { path ->
                    val file = java.io.File(path)
                    if (file.exists()) {
                        binding.ivHomeAvatar.setImageURI(android.net.Uri.fromFile(file))
                    }
                }

            }
        }
    }
// thay đổi liên tục phần home
    override fun onResume() {
        super.onResume()

        val userId =
            SessionManager(
                requireContext()
            )
                .getCurrentUserId()

        loadUserInfo(
            userId
        )
    }
    override fun onDestroyView() {

        super.onDestroyView()

        _binding =
            null
    }
    }