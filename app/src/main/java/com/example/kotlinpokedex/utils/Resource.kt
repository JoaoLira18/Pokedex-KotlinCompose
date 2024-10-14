package com.example.kotlinpokedex.utils
//
//sealed class Resource<T>(val data: T? = null, val message: MessageDialog? = null, val code : Int? = null, val loadingState: LoadingStates = LoadingStates.LOADING) {
//    class Success<T>(data: T) : Resource<T>(data = data)
//
//    class Error<T>(message: MessageDialog, code: Int = ErrorService.HTTP_500_INTERNAL_SERVER_ERROR) : Resource<T>( message = message, code = code)
//    class Loading<T>(state: LoadingStates = LoadingStates.LOADING) : Resource<T>(loadingState = state)
//}