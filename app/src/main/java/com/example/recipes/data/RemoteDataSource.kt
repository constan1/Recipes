package com.example.recipes.data

import com.example.recipes.data.network.FoodRecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor (
    private val foodRecipeApi: FoodRecipesApi
)
{
   suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe>
    {
        return foodRecipeApi.getRecipes(queries)
    }
}