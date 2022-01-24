package com.example.tierdex.data

import com.example.tierdex.R
import com.example.tierdex.model.Animal

class AnimalDataSource {
    fun loadAnimals(): List<Animal>{
        return listOf(
            Animal(R.string.animal1, R.drawable.image1),
            Animal(R.string.animal2, R.drawable.image2)
        )
    }
}