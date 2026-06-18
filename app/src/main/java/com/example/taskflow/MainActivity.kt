package com.example.taskflow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.taskflow.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private companion object {
        const val NOTIFICATION_PERMISSION_REQUEST = 100
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as NavHostFragment

        val navController =
            navHostFragment.navController

        // Bottom Navigation mặc định
        binding.bottomNav.setupWithNavController(
            navController
        )

        // Ẩn/hiện Bottom Navigation và FAB
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.splashFragment,
                R.id.loginFragment,
                R.id.registerFragment -> {

                    binding.bottomNav.visibility = View.GONE
                    binding.fabAddTask.visibility = View.GONE

                }

                else -> {

                    binding.bottomNav.visibility = View.VISIBLE
                    binding.fabAddTask.visibility = View.VISIBLE

                }

            }

        }

        // FAB thêm Task
        binding.fabAddTask.setOnClickListener {

            if (navController.currentDestination?.id != R.id.addTaskFragment) {

                navController.navigate(
                    R.id.addTaskFragment
                )

            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST
                )

            }

        }

    }

}