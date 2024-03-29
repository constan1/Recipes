package com.example.recipes.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.recipes.data.FoodRecipe
import com.example.recipes.data.Repository
import com.example.recipes.data.database.entities.FavoritesEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.data.database.entities.RecipesEntity
import com.example.recipes.models.FoodJoke
import com.example.recipes.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(

    private val repository: Repository,
    application:Application): AndroidViewModel(application) {

    /** ROOM DATABASE  */
    var readRecipe: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    var readFavoriteRecipes: LiveData<List<FavoritesEntity>> = repository.local.readFavoritesRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()


    private fun insertRecipes(recipesEntity: RecipesEntity) =
            viewModelScope.launch (Dispatchers.IO){
                repository.local.insertRecipes(recipesEntity)
            }

     fun insertFavoriteRecipe(favouritesEntity: FavoritesEntity) =

        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favouritesEntity)
        }

     fun deleteFavoriteRecipe(favouritesEntity: FavoritesEntity) =

        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favouritesEntity)
        }


     fun deleteAllFavoriteRecipes() =

        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
            viewModelScope.launch(Dispatchers.IO){
                repository.local.insertFoodJoke(foodJokeEntity)
    }



    /**Retrofit */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipSafeCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }



    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection())
        {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)


                val  foodRecipe = recipesResponse.value!!.data
                if(foodRecipe!=null)
                {
                    offlineCacheRecipe(foodRecipe)
                }
            }catch (e: Exception)
            {
            recipesResponse.value = NetworkResult.Error("Recipes not found")
            }


        }
        else
        {
            recipesResponse.value = NetworkResult.Error("No internet Connection")
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {

        foodJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection())
        {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)
                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke != null)
                {
                    offlineCacheFoodJoke(foodJoke)
                }

            }catch (e: Exception)
            {
                foodJokeResponse.value = NetworkResult.Error("Joke found")
            }


        }
        else
        {
            foodJokeResponse.value = NetworkResult.Error("No internet Connection")
        }
    }


    private suspend fun searchRecipSafeCall(searchQuery: Map<String, String>) {
        searchedRecipeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection())
        {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
               searchedRecipeResponse.value = handleFoodRecipesResponse(response)


            }catch (e: Exception)
            {
                searchedRecipeResponse.value = NetworkResult.Error("Recipes not found")
            }


        }
        else
        {
            searchedRecipeResponse.value = NetworkResult.Error("No internet Connection")
        }
    }


    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
            val recipesEntity = RecipesEntity(foodRecipe)
            insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    //error handling of request
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API key limited.")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else ->
            {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else ->false
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {

        return when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API key limited.")
            }

            response.isSuccessful -> {
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else ->
            {
                return NetworkResult.Error(response.message())
            }
        }
    }



}