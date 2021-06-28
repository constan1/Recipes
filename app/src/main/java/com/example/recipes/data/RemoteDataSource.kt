package com.example.recipes.data

import com.example.recipes.data.network.FoodRecipesApi
import com.example.recipes.models.FoodJoke
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

    suspend fun searchRecipes(searchQuery: Map<String, String>) : Response<FoodRecipe>
    {
        return foodRecipeApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke>
    {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}