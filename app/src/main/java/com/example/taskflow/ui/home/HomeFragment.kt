package com.example.taskflow.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment(
    R.layout.fragment_home
) {

    private var _binding:
            FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter:
            TaskAdapter

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

        adapter =
            TaskAdapter(
                emptyList()
            ) { task ->

                val bundle = Bundle().apply {
                    putInt(
                        "taskId",
                        task.id
                    )
                }

                findNavController().navigate(
                    R.id.action_homeFragment_to_taskDetailFragment,
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
            ).getCurrentUserId()

        viewModel
            .getTasksByUser(userId)
            .observe(viewLifecycleOwner) {
                    tasks ->
                adapter.updateTasks(
                    tasks
                )
            }

        binding.btnAddTask
            .setOnClickListener {
                findNavController().navigate(
                    R.id.action_homeFragment_to_addTaskFragment
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}