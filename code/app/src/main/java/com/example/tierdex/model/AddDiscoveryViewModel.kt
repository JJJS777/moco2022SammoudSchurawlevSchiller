package com.example.tierdex

import androidx.lifecycle.*
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Coordinates
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.launch

class AddDiscoveryViewModel(private val discoveredDao: DiscoveredDao): ViewModel( ) { //hier ggf. mit AndroidViewModel arbeiten


    //ToDo wofür nutzen?
    val allDiscoveries: LiveData<List<Discovered>> = discoveredDao.getDiscoveries().asLiveData()

    fun addNewDiscovery( discoName: String,
                         description: String,
                         coordinates: Coordinates,
                         camPicture: String,
                         country: String,
                         city: String,
                         postcode: String )
    {
        val newDiscovery = getNewDiscoEntry( discoName, description, coordinates,
            camPicture, country, city, postcode )
        insertDiscovery( newDiscovery )
    }

    private fun insertDiscovery( discovery: Discovered){
        viewModelScope.launch {
            discoveredDao.insert( discovery )
        }
    }

    //TODO später sollen die Daten aus dem Internet gefached werden und in Room abgespeichert
    private fun getNewDiscoEntry( discoName: String,
                                  description: String,
                                  coordinates: Coordinates,
                                  camPicture: String,
                                  country: String,
                                  city: String,
                                  postcode: String ) : Discovered
    {
        return Discovered(
            animalName = discoName,
            description = description,
            coordinates = coordinates,
            camPicture = camPicture,
            country = country,
            city = city,
            postcode = postcode
        )
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
