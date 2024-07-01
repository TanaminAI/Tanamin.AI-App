package com.dicoding.tanaminai.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.pref.UserModel
import com.dicoding.tanaminai.view.factory.AuthViewModelFactory
import com.dicoding.tanaminai.view.settings.SettingsViewModel
import com.dicoding.tanaminai.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.getSession().observe(this) { user: UserModel ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
    }
}