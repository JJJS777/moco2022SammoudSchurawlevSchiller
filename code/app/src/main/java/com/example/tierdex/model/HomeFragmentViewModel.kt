package com.example.tierdex.model

import androidx.lifecycle.*
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.roomDB.dao.DiscoveredDao
import com.example.tierdex.roomDB.entities.Coordinates
import com.example.tierdex.roomDB.entities.Discovered
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val discoveredDao: DiscoveredDao): ViewModel( ) {

    val allDiscoveries: LiveData<List<Discovered>> = discoveredDao.getDiscoveries().asLiveData()

    //Nicht Implementiert
    fun deleteDisco( disco: Discovered){ viewModelScope.launch { discoveredDao.delete(disco) } }

    fun retrieveDisco( id: Int ): LiveData<Discovered> = discoveredDao.getDiscovery(id).asLiveData()

    private fun updateDisco( disco : Discovered){
        viewModelScope.launch {
            discoveredDao.upadete(disco)
        }
    }

    private fun  getUpdatedDiscoEntry(
        discoID: Int,
        discoName: String,
        description: String,
        coordinates: Coordinates,
        camPicture: String,
        country: String,
        city: String,
        postcode: String
    ): Discovered {
        return Discovered(
            discoID = discoID,
            animalName = discoName,
            description = description,
            coordinates = coordinates,
            camPicture = camPicture,
            country = country,
            city = city,
            postcode = postcode,
            userID = "1",
            imageUrl = "empty"
        )
    }

    // Nicht Genutzt, ist vorgesehen für Änderungen
    fun updateDisco(
        discoID: Int,
        discoName: String,
        description: String,
        coordinates: Coordinates,
        camPicture: String,
        country: String,
        city: String,
        postcode: String
    ){
        val updatedDisco = getUpdatedDiscoEntry(discoID, discoName, description, coordinates,
            camPicture, country, city, postcode)
        updateDisco(updatedDisco)
    }

    //TODO fun isEntryValid() implementieren
}

class AddDiscoveryViewModelFactory( private val discoveredDao: DiscoveredDao )
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDiscoveryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddDiscoveryViewModel(discoveredDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}