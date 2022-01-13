package com.example.tierdex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import kotlinx.android.synthetic.main.activity_loading.*

class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

//        progress_circular.animate().setDuration(1500).alpha(1f).withEndAction{
//            val i = Intent(this, MainActivity::class.java)
//            startActivity(i)
//        }
    }
}