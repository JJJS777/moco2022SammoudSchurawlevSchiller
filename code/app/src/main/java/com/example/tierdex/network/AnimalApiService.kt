package com.example.tierdex.network

import com.example.tierdex.model.AnimalData
import com.example.tierdex.model.ApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AnimalApiService {
    @GET("photos")
    //TODO Quaery Parameter einfügen...
    suspend fun getData(): ApiResponse
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AnimalApi {
    val retrofitService: AnimalApiService by lazy { retrofit.create(AnimalApiService::class.java) }
}