package com.example.recipes.data.database

import androidx.room.*
import com.example.recipes.data.database.entities.FavoritesEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.data.database.entities.RecipesEntity
import com.example.recipes.models.FoodJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favouritesEntity: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
     fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favourite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favourite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()





}