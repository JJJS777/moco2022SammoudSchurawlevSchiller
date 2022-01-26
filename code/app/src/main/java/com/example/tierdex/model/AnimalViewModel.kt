package com.example.tierdex.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierdex.network.AnimalApi
import kotlinx.coroutines.launch
import java.lang.Exception

enum class AnimalApiStatus { LOADING, ERROR, DONE  }

class AnimalViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<AnimalApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<AnimalApiStatus> = _status

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> = _animals

    init {
        getAnimalData()
    }

    private fun getAnimalData(){

        viewModelScope.launch {
            _status.value = AnimalApiStatus.LOADING
            try {
                _animals.value = AnimalApi.retrofitService.getData()
                _status.value = AnimalApiStatus.DONE
            } catch (e: Exception){
                _status.value = AnimalApiStatus.ERROR
                _animals.value = listOf()
            }
        }
    }
}