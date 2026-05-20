package com.example.taskflow.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.UserRepository
import com.example.taskflow.databinding.FragmentProfileBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.AuthViewModel
import com.example.taskflow.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(
    R.layout.fragment_profile
) {

    private var _binding:
            FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager:
            SessionManager

    private val userRepository:
            UserRepository by lazy {
        UserRepository(
            DatabaseProvider
                .getDatabase(
                    requireContext()
                )
                .userDao()
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
            FragmentProfileBinding.bind(
                view
            )

        sessionManager =
            SessionManager(
                requireContext()
            )

        val userId =
            sessionManager
                .getCurrentUserId()

        CoroutineScope(
            Dispatchers.Main
        ).launch {

            val user =
                userRepository
                    .getUserById(
                        userId
                    )

            user?.let {
                binding.tvUsername.text =
                    it.username

                binding.tvEmail.text =
                    it.email
            }
        }

        binding.btnLogout
            .setOnClickListener {

                sessionManager.logout()

                findNavController()
                    .navigate(
                        R.id.action_profileFragment_to_loginFragment
                    )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}