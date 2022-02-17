package com.example.tierdex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.launch

class RoomViewModel(private val discoveredDao: DiscoveredDao,
                    app: Application)
    : AndroidViewModel( app ) { //hier ggf. mit AndroidViewModel arbeiten


    val allDiscoveries: LiveData<List<Discovered>> = discoveredDao.getDiscoveries().asLiveData()

    fun addNewDiscovery( discoName: String ){
        val newDiscovery = getNewDiscoEntry( discoName )
        insertDiscovery( newDiscovery )
    }

    private fun insertDiscovery( discovery: Discovered ){
        viewModelScope.launch {
            discoveredDao.insert( discovery )
        }
    }

    //TODO später sollen die Daten aus dem Internet gefached werden und in Room abgespeichert
    private fun getNewDiscoEntry( discoName: String ) : Discovered{
        return Discovered(
            discoName = discoName
        )
    }

    fun retrieveDisco( id: Int ): LiveData<Discovered> = discoveredDao.getDiscovery(id).asLiveData()

    private fun updateDisco( disco : Discovered ){
        viewModelScope.launch {
            discoveredDao.upadete(disco)
        }
    }

    fun deleteDisco( disco: Discovered ){ viewModelScope.launch { discoveredDao.delete(disco) } }

    private fun  getUpdatedDiscoEntry(
        discoID: Int,
        discoName: String
    ): Discovered {
        return Discovered(
            discoID = discoID,
            discoName = discoName
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

//class TierDexViewModelFactory( private val animalDao: AnimalDao,
//                               private val discoveredDao: DiscoveredDao )
//    : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TierDexViewModelFactory::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return TierDexViewModel(animalDao, discoveredDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//}