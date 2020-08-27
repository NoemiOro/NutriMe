package me.erika.retrofitexample.repository

import com.google.gson.GsonBuilder
import me.erika.retrofitexample.network.FoodAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NutritionRepository() {

    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://edamam-food-and-grocery-database.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(FoodAPI::class.java)
    }

    //We don’t need to call enqueue() and implement callbacks anymore!
    //Now repo method is suspend too and returns an item object.
    suspend fun getFoodFromService(item: String): FoodAPIResult {
        try {
            return FoodAPIResult.Success(response = webservice.getFoodAPI(item))
        }
        catch (ex: Exception){
           return FoodAPIResult.Error(
                message = "Server request failed due to an exception. Message: ${ex.message}",
                cause = ex
            )
        }
    }
}

//  fun getFoodFromService(item: String) {

//Old Retrofit way
//        //logging http client to monitor response
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        val httpClient = OkHttpClient.Builder()
//        httpClient.addInterceptor(logging);
//
//        //Retrofit build call
//        var gson = GsonBuilder().create()
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://edamam-food-and-grocery-database.p.rapidapi.com")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(httpClient.build())
//            .build()
//        val service = retrofit.create(FoodAPI::class.java)
//        var api = service.getFoodAPI(item)
//
//        //Send request asynchronously and notify response
//        api.enqueue(object : Callback<FoodAPIResponse> {
//            override fun onFailure(call: Call<FoodAPIResponse>, t: Throwable) {
//                Log.e("Erika", "Error calling Food service $t message: " + t.message)
//            }
//
//            override fun onResponse(call: Call<FoodAPIResponse>, response: Response<FoodAPIResponse>) {
//                Log.d("Erika", "yay " + response.body().toString())
//            }
//        })
//   }