package com.example.recipes.data

import com.example.recipes.data.database.RecipesDao
import com.example.recipes.data.database.entities.FavoritesEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.data.database.entities.RecipesEntity
import com.example.recipes.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource@Inject constructor(
    private val recipesDao : RecipesDao
    ) {
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readFavoritesRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDao.readFoodJoke()
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)
    {
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)
    {
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }
    suspend fun deleteAllFavoriteRecipes()
    {
        recipesDao.deleteAllFavoriteRecipes()
    }

}