package me.erika.retrofitexample.network

import me.erika.retrofitexample.repository.FoodAPIResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FoodAPI {

    @Headers(
        "X-RapidAPI-Host: edamam-food-and-grocery-database.p.rapidapi.com",
        "X-RapidAPI-Key: c977c7c52amsh02ac9404b58b8c4p1ea727jsn8582dbbc0df2"
    )

    //With retrofit support for courrutines, changed call<FoodAPIResponse> to suspend
    @GET("/parser")
    suspend fun getFoodAPI(@Query("ingr") ingr: String?): FoodAPIResponse
}


//https://rapidapi.com/edamam/api/edamam-food-and-grocery-database