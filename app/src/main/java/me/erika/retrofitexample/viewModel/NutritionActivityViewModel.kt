package me.erika.retrofitexample.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.erika.retrofitexample.R
import me.erika.retrofitexample.repository.*

class NutritionActivityViewModel : ViewModel() {

    val nutritionRepository = NutritionRepository()
    private val _nutrientsLiveData = MutableLiveData<ArrayList<NutrientsDTO?>>()
    val nutrientsLiveData get() = _nutrientsLiveData
    private val _otherNutritionInfo = MutableLiveData<Map<Int, String>>()
    val otherNutritionInfo get() = _otherNutritionInfo
    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage get() = _errorMessage

    /* //The suspend functions can only be called from Coroutines.

   // Adding the keyword suspend helps the coroutine to suspend (pause), perform the required job on a network thread
    // (if Dispatchers.IO) is used, wait for the response, and then resumes from where it left off once the response is available.
    //only once the network call(which is run on another thread, in this case, the thread from Dispatchers.IO)
    // is completed (success or error), the coroutine resumes by emitting the respective value that is obtained from the network call.
    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    } */
    fun getFood(item: String) {
        viewModelScope.launch{
            val result = nutritionRepository.getFoodFromService(item)
            when (result) {
                is FoodAPIResult.Success -> {
                    Log.i("Erika", "Success ${result.response}")
                    if(result.response.parsed.isNotEmpty()) {
                        _nutrientsLiveData.postValue(
                            populateNutrientsArray(
                                result.response.parsed.get(
                                    0
                                ).food.nutrients
                            )
                        )
                        _otherNutritionInfo.postValue(
                            populateOtherNutritionInfo(
                                result.response.parsed.get(
                                    0
                                )
                            )
                        )
                    }
                    else{
                       _nutrientsLiveData.postValue(ArrayList())
                        _otherNutritionInfo.postValue(mutableMapOf())
                        _errorMessage.postValue(R.string.not_found)
                    }
                }
                is FoodAPIResult.Error -> {
                    Log.i("Erika", "Failure  ${result.message}   ${result.cause}")
                    _errorMessage.postValue(R.string.error_message)
                }
            }
        }
    }

    //Wasn't needed to add these to arrayList, implemented this way only to use/practice Recycler
    private fun populateNutrientsArray(it: Nutrients): ArrayList<NutrientsDTO?> {
        val nutrients = ArrayList<NutrientsDTO?>()
        nutrients.add(NutrientsDTO(R.string.calories, it.enerCal.toString()))
        nutrients.add(NutrientsDTO(R.string.protein, it.procnt.toString()))
        nutrients.add(NutrientsDTO(R.string.carbo, it.chocdf.toString()))
        nutrients.add(NutrientsDTO(R.string.fat, it.fat.toString()))
        nutrients.add(NutrientsDTO(R.string.fiber, it.fibtg.toString()))
        return nutrients
    }

    private fun populateOtherNutritionInfo(info: Parsed): Map<Int,String> {
        val otherInfo = mutableMapOf<Int,String>()
        otherInfo.put(R.string.label,info.food.label)
        otherInfo.put(R.string.category,info.food.categoryLabel)
        otherInfo.put(R.string.image, info.food.image)
        return otherInfo
    }

}