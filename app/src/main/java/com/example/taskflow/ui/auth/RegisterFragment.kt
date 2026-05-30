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
import com.example.taskflow.data.local.entity.UserEntity
import com.example.taskflow.data.repository.UserRepository
import com.example.taskflow.databinding.FragmentRegisterBinding
import com.example.taskflow.viewmodel.AuthViewModel
import com.example.taskflow.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding:
            FragmentRegisterBinding? = null
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
            FragmentRegisterBinding.inflate(
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

        binding.btnRegister
            .setOnClickListener {

                val name =
                    binding.etName.text
                        .toString()
                        .trim()

                val email =
                    binding.etEmail.text
                        .toString()
                        .trim()

                val password =
                    binding.etPassword.text
                        .toString()
                        .trim()

                val confirmPassword =
                    binding.etConfirmPassword.text
                        .toString()
                        .trim()

                if (
                    name.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (password != confirmPassword) {
                    Toast.makeText(
                        requireContext(),
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.checkEmailExists(
                    email
                ) { exists ->

                    if (exists) {
                        Toast.makeText(
                            requireContext(),
                            "Email already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@checkEmailExists
                    }

                    val user =
                        UserEntity(
                            username = name,
                            email = email,
                            password = password
                        )

                    viewModel.registerUser(
                        user
                    ) { result ->

                        if (result > 0) {
                            Toast.makeText(
                                requireContext(),
                                "Register success",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController()
                                .navigate(
                                    R.id.action_registerFragment_to_loginFragment
                                )

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Register failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}