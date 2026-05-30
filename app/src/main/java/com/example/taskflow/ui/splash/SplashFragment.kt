package com.example.taskflow.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.session.SessionManager

class SplashFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_splash,
            container,
            false
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

        sessionManager =
            SessionManager(
                requireContext()
            )

        Handler(
            Looper.getMainLooper()
        ).postDelayed({

            if (!isAdded) return@postDelayed

            val navController =
                findNavController()

            val currentDestination =
                navController.currentDestination?.id

            if (currentDestination !=
                R.id.splashFragment
            ) {
                return@postDelayed
            }

            if (sessionManager.isLoggedIn()) {

                navController.navigate(
                    R.id.action_splashFragment_to_homeFragment
                )

            } else {

                navController.navigate(
                    R.id.action_splashFragment_to_loginFragment
                )
            }

        }, 1500)
    }
}