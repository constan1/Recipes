package com.example.recipes.data

import com.example.recipes.models.Result
import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>

)