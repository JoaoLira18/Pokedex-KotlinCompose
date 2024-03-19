package com.example.kotlinpokedex.data.pagination

interface Paging {
    suspend fun loadNextItems()
    fun reset()
}