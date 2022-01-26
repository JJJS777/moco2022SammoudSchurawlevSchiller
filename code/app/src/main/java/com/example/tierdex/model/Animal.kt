package com.example.tierdex.model

import com.squareup.moshi.Json

data class Animal(
    val animalID: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String ) {}