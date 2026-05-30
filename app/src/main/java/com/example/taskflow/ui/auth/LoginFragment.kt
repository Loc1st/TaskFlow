package com.example.taskflow.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.repository.UserRepository
import com.example.taskflow.databinding.FragmentLoginBinding
import com.example.taskflow.session.SessionManager
import com.example.taskflow.viewmodel.AuthViewModel
import com.example.taskflow.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment() {

    private var _binding:
            FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel:
            AuthViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository(
                DatabaseProvider
                    .getDatabase(
                        requireContext()
                    )
                    .userDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentLoginBinding.inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        binding.btnLogin
            .setOnClickListener {

                val email =
                    binding.etEmail.text
                        .toString()
                        .trim()

                val password =
                    binding.etPassword.text
                        .toString()
                        .trim()

                if (
                    email.isEmpty() ||
                    password.isEmpty()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.login(
                    email,
                    password
                ) { user ->

                    if (user != null) {

                        SessionManager(
                            requireContext()
                        ).saveLoginSession(
                            user.id
                        )

                        findNavController()
                            .navigate(
                                R.id.action_loginFragment_to_homeFragment
                            )

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Invalid account",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        binding.tvRegister
            .setOnClickListener {
                findNavController()
                    .navigate(
                        R.id.action_loginFragment_to_registerFragment
                    )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}