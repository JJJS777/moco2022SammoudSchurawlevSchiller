package com.example.tierdex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tierdex.databinding.ActivityMainBinding
import com.example.tierdex.fragments.AnimalListFragment
import com.example.tierdex.fragments.HomeFragment
import com.example.tierdex.fragments.SecondFragment
import com.example.tierdex.fragments.ThirdFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> frameSwitch(AnimalListFragment())
                R.id.ic_input -> frameSwitch(SecondFragment())
                R.id.ic_input2 -> frameSwitch(ThirdFragment())
            }
            true
        }
        frameSwitch(HomeFragment())
    }

    private fun frameSwitch(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }

}