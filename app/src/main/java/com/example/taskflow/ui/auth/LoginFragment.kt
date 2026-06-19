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
import com.example.taskflow.databinding.FragmentLoginBinding
import com.example.taskflow.data.firebase.FirebaseAuthRepository
class LoginFragment : Fragment() {

    private var _binding:
            FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authRepository = FirebaseAuthRepository()

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
        android.util.Log.d(
            "AUTH",
            FirebaseAuthRepository().getCurrentUid() ?: "NULL"
        )

        binding.btnLogin.setOnClickListener {

            val email =
                binding.etEmail.text
                    .toString()
                    .trim()

            val password =
                binding.etPassword.text
                    .toString()
                    .trim()

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            authRepository.login(
                email,
                password
            ) { success, message ->

                if (success) {

                    findNavController().navigate(
                        R.id.homeFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(
                                R.id.loginFragment,
                                true
                            )
                            .build()
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

        binding.tvRegister.setOnClickListener {

            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
