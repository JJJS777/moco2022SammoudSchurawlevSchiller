package com.example.tierdex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tierdex.data.dao.AnimalDao
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Animals
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.launch

class TierDexViewModel( private val animalDao: AnimalDao, private val discoveredDao: DiscoveredDao)
    : ViewModel() { //hier ggf. mit AndroidViewModel arbeiten


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

    fun addNewAnimal(animalSpecies: String, animalFamily: String, animalOrder: String,
                     animalClass: String, animalRegion: String) {
        val newAnimal = getNewAnimalEntry( animalSpecies, animalFamily, animalOrder, animalClass,
            animalRegion )
        insertAnimal(newAnimal)
    }


    private fun insertdiscovery( discovered: Discovered ){
        //TODO
    }


}

class TierDexViewModelFactory( private val animalDao: AnimalDao,
                               private val discoveredDao: DiscoveredDao )
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TierDexViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TierDexViewModel(animalDao, discoveredDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}