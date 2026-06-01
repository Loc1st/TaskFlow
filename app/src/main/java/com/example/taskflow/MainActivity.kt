package com.example.taskflow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.taskflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityMainBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        binding =
            ActivityMainBinding.inflate(
                layoutInflater
            )

        setContentView(
            binding.root
        )

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(
                    R.id.nav_host_fragment
                ) as NavHostFragment

        val navController =
            navHostFragment.navController

        binding.bottomNav
            .setupWithNavController(
                navController
            )

        // mới
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registerFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.fabAddTask.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.fabAddTask.visibility = View.VISIBLE
                }
            }
        }

        // 2. Gắn sự kiện click cho nút "+" (FAB) để chuyển hướng đến màn hình thêm task
        binding.fabAddTask.setOnClickListener {
            navController.navigate(R.id.addTaskFragment)
        }
    }
}