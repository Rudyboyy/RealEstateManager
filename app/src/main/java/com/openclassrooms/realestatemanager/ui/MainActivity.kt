package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MainActivityBinding
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}