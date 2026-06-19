package com.example.taskflow.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.databinding.FragmentRegisterBinding
import com.example.taskflow.data.firebase.FirebaseAuthRepository
class RegisterFragment : Fragment() {

    private var _binding:
            FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authRepository =
        FirebaseAuthRepository()

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

                authRepository.register(

                    username = name,

                    email = email,

                    password = password

                ) { success, message ->

                    if (success) {

                        Toast.makeText(

                            requireContext(),

                            "Đăng ký thành công",

                            Toast.LENGTH_SHORT

                        ).show()

                        findNavController().navigate(

                            R.id.action_registerFragment_to_loginFragment

                        )

                    } else {

                        Toast.makeText(

                            requireContext(),

                            message,

                            Toast.LENGTH_SHORT

                        ).show()

                    }

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}