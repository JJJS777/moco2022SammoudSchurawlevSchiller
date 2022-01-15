package com.example.tierdex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tierdex.data.dao.AnimalDao
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.dao.UserDao
import com.example.tierdex.data.entities.Animals
import com.example.tierdex.data.entities.Discovered
import com.example.tierdex.data.entities.Users
import kotlinx.coroutines.launch

class TierDexViewModel( private val animalDao: AnimalDao, private val userDao: UserDao,
                        private val discoveredDao: DiscoveredDao)
    : ViewModel() {

    //todo: ohne ende Redundanz, wie beheben??
    private fun insertAnimal( animals: Animals){
        viewModelScope.launch {
            animalDao.insert( animals )
        }
    }

    private fun insertUser( user: Users ){
        viewModelScope.launch {
            userDao.insert( user )
        }
    }

    private fun insertDiscovery( discovery: Discovered ){
        viewModelScope.launch {
            discoveredDao.insert( discovery )
        }
    }

    //TODO später sollen die Daten aus dem Internet gefached werden und in Room abgespeichert
    private fun getNewAnimalEntry( animalSpecies: String, animalFamily: String, animalOrder: String,
                                   animalClass: String ) : Animals{
        return Animals(
            animalSpecies = animalSpecies,
            animalFamily = animalFamily,
            animalOrder = animalOrder,
            animalClass = animalClass
        )
    }

    fun addNewAnimal(animalSpecies: String, animalFamily: String, animalOrder: String,
                     animalClass: String) {
        val newAnimal = getNewAnimalEntry( animalSpecies, animalFamily, animalOrder, animalClass )
        insertAnimal(newAnimal)
    }


    private fun insertdiscovery( discovered: Discovered ){
        //TODO
    }


}

class TierDexViewModelFactory( private val animalDao: AnimalDao, private val userDao: UserDao,
                               private val discoveredDao: DiscoveredDao )
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TierDexViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TierDexViewModel(animalDao, userDao, discoveredDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}