package com.example.tierdex.model

import android.app.appsearch.SearchResults
import com.squareup.moshi.Json

data class response(
    val searchResults: SearchResults
)

data class searchResults(
    val results: List<Animal>
)

data class Animal(
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "imageUrl") val imgSrcUrl: String ) {}