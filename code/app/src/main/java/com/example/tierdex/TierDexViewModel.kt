package com.example.tierdex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tierdex.data.dao.AnimalDao
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Animals
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.launch
import java.sql.Date

class TierDexViewModel( private val animalDao: AnimalDao, private val discoveredDao: DiscoveredDao,
                        app: Application)
    : AndroidViewModel( app ) { //hier ggf. mit AndroidViewModel arbeiten


    private fun insertAnimal( animals: Animals){
        viewModelScope.launch {
            animalDao.insert( animals )
        }
    }

    private fun insertDiscovery( discovery: Discovered ){
        viewModelScope.launch {
            discoveredDao.insert( discovery )
        }
    }

    //TODO später sollen die Daten aus dem Internet gefached werden und in Room abgespeichert
    private fun getNewAnimalEntry( animalSpecies: String, animalFamily: String, animalOrder: String,
                                   animalClass: String, animalRegion: String ) : Animals{
        return Animals(
            animalSpecies = animalSpecies,
            animalFamily = animalFamily,
            animalOrder = animalOrder,
            animalClass = animalClass,
            animalRegion = animalRegion
        )
    }

    fun addNewAnimal( animalSpecies: String, animalFamily: String, animalOrder: String,
                     animalClass: String, animalRegion: String ) {
        val newAnimal = getNewAnimalEntry( animalSpecies, animalFamily, animalOrder, animalClass,
            animalRegion )
        insertAnimal(newAnimal)
    }


    private fun getNewDiscovery( discoveryTime: String, is_alive: String )
    : Discovered {
        return Discovered(
            discoveryTime = discoveryTime,
            is_alive = is_alive
        )
    }

    fun addNewDiscovery( discoveryTime: String , is_alive: String ){
        val newDiscovery = getNewDiscovery( discoveryTime ,is_alive )
        insertDiscovery( newDiscovery )
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