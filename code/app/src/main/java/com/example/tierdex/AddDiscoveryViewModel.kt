package com.example.tierdex

import androidx.lifecycle.*
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.launch

class AddDiscoveryViewModel(private val discoveredDao: DiscoveredDao): ViewModel( ) { //hier ggf. mit AndroidViewModel arbeiten


    val allDiscoveries: LiveData<List<Discovered>> = discoveredDao.getDiscoveries().asLiveData()

    fun addNewDiscovery( discoName: String ){
        val newDiscovery = getNewDiscoEntry( discoName )
        insertDiscovery( newDiscovery )
    }

    private fun insertDiscovery( discovery: Discovered){
        viewModelScope.launch {
            discoveredDao.insert( discovery )
        }
    }

    //TODO später sollen die Daten aus dem Internet gefached werden und in Room abgespeichert
    private fun getNewDiscoEntry( discoName: String ) : Discovered {
        return Discovered(
            animalName = discoName
        )
    }

    fun retrieveDisco( id: Int ): LiveData<Discovered> = discoveredDao.getDiscovery(id).asLiveData()

    private fun updateDisco( disco : Discovered){
        viewModelScope.launch {
            discoveredDao.upadete(disco)
        }
    }

    fun deleteDisco( disco: Discovered){ viewModelScope.launch { discoveredDao.delete(disco) } }

    private fun  getUpdatedDiscoEntry(
        discoID: Int,
        discoName: String
    ): Discovered {
        return Discovered(
            discoID = discoID,
            animalName = discoName
        )
    }

    fun updateDisco(
        discoID: Int,
        discoName: String
    ){
        val updatedDisco = getUpdatedDiscoEntry(discoID, discoName)
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
