package com.example.tierdex.model

import com.squareup.moshi.Json

data class ApiResponse(
    val searchResults: ResSearchResults
)

data class ResSearchResults(
    val totalRecords: String,
    val results: List<AnimalData>
)


//Data Attribute von Tieren
data class AnimalData(
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "imageUrl") val imgSrcUrl: String,
    val name: String,
    @Json (name = "scientificName") val binomialName: String?,
    @Json(name = "acceptedConceptName") val binomialNameAlt: String? )