package com.example.taskflow.ui.priority

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.databinding.FragmentPriorityTasksBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory

class PriorityTasksFragment : Fragment(
    R.layout.fragment_priority_tasks
) {

    private var _binding:
            FragmentPriorityTasksBinding? = null

    private val binding
        get() = _binding!!


    private lateinit var adapter:
            PriorityTaskAdapter


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
            FragmentPriorityTasksBinding
                .bind(view)



        val priority =

            arguments
                ?.getString(
                    "priority"
                )
                ?: "high"



        binding.tvPriorityTitle.text =

            priority.uppercase() +
                    " PRIORITY"



        adapter =

            PriorityTaskAdapter {

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



        binding.rvTasks.layoutManager =

            LinearLayoutManager(
                requireContext()
            )

        binding.rvTasks.adapter =
            adapter



        val userId =

            SessionManager(
                requireContext()
            )
                .getCurrentUserId()



        viewModel
            .getTasksByPriority(
                userId,
                priority
            )
            .observe(
                viewLifecycleOwner
            ) {

                adapter.update(
                    it
                )
            }

    }



    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}