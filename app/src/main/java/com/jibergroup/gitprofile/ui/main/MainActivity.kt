package com.jibergroup.gitprofile.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jibergroup.gitprofile.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.searchFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView.setupWithNavController(navController)
    }
}