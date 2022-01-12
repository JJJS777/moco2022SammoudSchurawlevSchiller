package com.example.tierdex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tierdex.fragments.HomeFragment
import com.example.tierdex.fragments.SecondFragment
import com.example.tierdex.fragments.ThirdFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> frameSwitch(HomeFragment())
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