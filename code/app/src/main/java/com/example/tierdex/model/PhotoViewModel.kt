package com.example.tierdex.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Photo(val uri: Uri)

class PhotoViewModel : ViewModel()
{
    private val mutableSelectedItem = MutableLiveData<Photo>()
    val selectedItem: LiveData<Photo> get() = mutableSelectedItem

    fun addItem(photo: Photo) {
        mutableSelectedItem.postValue(photo)
    }
}
