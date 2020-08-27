package me.erika.retrofitexample.repository

import com.google.gson.annotations.SerializedName

data class FoodAPIResponse (
    val text : String,
    val parsed : List<Parsed>
)

data class Parsed (
    val food : Food
)

data class Food (
    val foodId : String,
    val uri : String,
    val label : String,
    val nutrients : Nutrients,
    val category : String,
    val categoryLabel : String,
    val image : String
)

data class Nutrients(
    @SerializedName("ENERC_KCAL")
    val enerCal: String? = null,

    @SerializedName("PROCNT")
    val procnt: String? = null,

    @SerializedName("FAT")
    val fat: String? = null,

    @SerializedName("CHOCDF")
    val chocdf: String? = null,

    @SerializedName("FIBTG")
    val fibtg: String? = null
)

