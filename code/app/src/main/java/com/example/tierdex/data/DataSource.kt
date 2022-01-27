package com.example.tierdex.data
import com.example.tierdex.R
import com.example.tierdex.model.Post

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