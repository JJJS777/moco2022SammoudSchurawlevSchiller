package com.example.tierdex

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.tierdex.databinding.ActivityMainBinding
import com.example.tierdex.fragments.AnimalListFragment
import com.example.tierdex.fragments.HomeFragment
import com.example.tierdex.fragments.camera.CameraFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> frameSwitch(HomeFragment())
                R.id.ic_camera -> frameSwitch(CameraFragment())
                R.id.ic_document -> frameSwitch(AnimalListFragment())
            }
            true
        }
        // On Start
        frameSwitch(HomeFragment())
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    private fun frameSwitch(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }
}