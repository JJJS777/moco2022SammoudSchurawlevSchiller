package com.example.tierdex.model
import com.example.tierdex.R

class DataSource{
    fun loadPost() : List<Post>{
        return listOf(
            // needs loop?
            Post(R.string.User1, R.drawable.ic_launcher_background),
            Post(R.string.User1, R.drawable.ic_launcher_background),
            Post(R.string.User1, R.drawable.ic_launcher_background)
        )
    }
}