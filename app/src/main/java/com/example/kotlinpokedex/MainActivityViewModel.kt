package com.example.kotlinpokedex

import androidx.lifecycle.ViewModel
import com.example.kotlinpokedex.data.repository.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: PokedexRepository,
) : ViewModel() {

//    var loading = mutableStateOf(false)

//    private val _pets = MutableStateFlow<List<Animal>>(emptyList())
//    val pets = _pets.asStateFlow()
//
//    var pet = mutableStateOf<AnimalResponse?>(null)
//
//    var token = mutableStateOf<String>("")
//
//    fun getToken() {
//        viewModelScope.launch {
//            loading.value = true
//            error.value = null
//            when (val response = repository.getToken()) {
//                is Resource.Success -> {
//                    if (response.data != null)
//                        token.value = "Bearer ${response.data.access_token}"
//                }
//                is Resource.Error -> error.value = response.message
//            }
//        }
//    }

//    fun getAnimals() {
//        viewModelScope.launch {
//            loading.value = true
//            error.value = null
//            when (val response = repository.getAnimals(token.value)) {
//                is Resource.Success -> _pets.value = response.data?.animals ?: emptyList()
//                is Resource.Error -> error.value = response.message
//            }
//            loading.value = false
//        }
//    }
}