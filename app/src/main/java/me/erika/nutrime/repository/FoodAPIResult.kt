package me.erika.nutrime.repository

//Sealed class which will make our responses more even and easy to handle.
//Sealed classes only can be extended within their file
sealed class FoodAPIResult {
    data class Success(val response: FoodAPIResponse) : FoodAPIResult()
    data class Error(val message: String, val cause: Exception? = null) : FoodAPIResult()
}