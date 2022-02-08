package com.example.tierdex.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierdex.network.AnimalApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class AnimalApiStatus { LOADING, ERROR, DONE  }

class AnimalViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<AnimalApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<AnimalApiStatus> = _status

    private val _animalProperties = MutableLiveData<ApiResponse>()
    val animalProperties: LiveData<ApiResponse> = _animalProperties

    init {
        //getAnimalData()
    }


    val apiInterface = AnimalApi.retrofitService.getData().enqueue( object : Callback<ApiResponse?> {
        override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {
            if (response?.body() != null ) {
                _animalProperties.value = response.body()
                _status.value = AnimalApiStatus.DONE
                //onSearch()
            }

        }

        override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
            _status.value = AnimalApiStatus.ERROR
            Log.e("conn_err", t.message.toString())
        }
    })

    fun onSearch(){}


/*    private fun getAnimalData(){

        viewModelScope.launch {
            _status.value = AnimalApiStatus.LOADING
            try {

            } catch (e: Exception){
                _status.value = AnimalApiStatus.ERROR
                Log.e("conn_err", e.message.toString())
            }
        }
    }*/
}