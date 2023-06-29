package com.example.gogreen

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gogreen.databinding.ActivityMainBinding
import com.example.gogreen.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.go_green)
        binding.toolbar.setTitleTextColor(getColor(R.color.white))

        navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observeNavbar()
    }

    fun observeNavbar() {
        sharedViewModel.showBottomNavigation.value = true
        sharedViewModel.showBottomNavigation.observe(this) {
            binding.navView.visibility = if (it) View.VISIBLE else View.GONE
            binding.toolbar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) {
            supportFragmentManager.popBackStack()
            sharedViewModel.showBottomNavigation.value = true
//                supportActionBar!!.show()
        } else
            onBackPressedDispatcher.onBackPressed()
    }
}