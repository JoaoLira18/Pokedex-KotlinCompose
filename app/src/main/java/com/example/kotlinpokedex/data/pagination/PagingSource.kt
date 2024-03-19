package com.example.kotlinpokedex.data.pagination

import com.example.kotlinpokedex.data.api.AppException
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonList
import com.example.kotlinpokedex.data.models.PokemonUrl

class PagingSource(
    private inline val onLoadUpdate: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextPage: String?) -> ResponseResource<PokemonList>,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: (pokemons: List<Pokemon>, nextPokemonsUrl: String) -> Unit,
) : Paging {

    private var nextPage = null
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdate(true)
        val result = onRequest(nextPage)
        isMakingRequest = false
//        if (result is ResponseResource.Success) {
//            val pokemonsUrlList = pokemonUrlListResult.data.pokemons.map { pokemon ->
//                PokemonUrl(
//                    name = pokemon.name,
//                    url = pokemon.url
//                )
//            }
//            getPokemonsDetails(pokemonsUrlList)
//        } else {
//            _pokemons.value = ResponseResource.Error(AppException("Next Pokemons Url Result Error"))
//        }
        val requestResult = result
//        val requestResult = result.getOrElse {
//            onError(it)
//            onLoadUpdate(false)
//            return
//        }
//        onSuccess(requestResult, nextPage)
        onLoadUpdate(false)
    }

    override fun reset() {
        nextPage = null
    }
}

//class PagingSource<Key, Item>(
//    private val initialKey: Key,
//    private inline val onLoadUpdate: (Boolean) -> Unit,
//    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
//    private inline val getNextKey: (List<Item>) -> Key,
//    private inline val onError: suspend (Throwable?) -> Unit,
//    private inline val onSuccess: (items: List<Item>, newKey: Key) -> Unit,
//): Paging<Key, Item> {
//
//    private var currentKey = initialKey
//    private var isMakingRequest = false
//    override suspend fun loadNextItems() {
//        if(isMakingRequest) {
//            return
//        }
//        isMakingRequest = true
//        onLoadUpdate(true)
//        val result = onRequest(currentKey)
//        isMakingRequest = false
//        val items = result.getOrElse {
//            onError(it)
//            onLoadUpdate(false)
//            return
//        }
//        currentKey = getNextKey(items)
//        onSuccess(items, currentKey)
//        onLoadUpdate(false)
//    }
//
//    override fun reset() {
//        currentKey = initialKey
//    }
//}