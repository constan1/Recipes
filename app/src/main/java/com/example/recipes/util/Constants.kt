package com.example.recipes.util

class Constants {

    companion object {
        const val API_KEY = "d0b547f4b16d4810b7218e3f754a9fbd"
        const val BASE_URL = "https://api.spoonacular.com"

        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        const val  RECIPE_RESULT_KEY = "recipeBundle"

        //API Query Keys

        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM database
        const val DATABASE_NAME = "Recipes_Database"
        const val RECIPES_TABLE = "Recipes_Table"
        const val FAVOURITE_RECIPES_TABLE = "favourite_recipes_table"

        //Bottom Sheet preferences

        const val DEFAULT_RECIPE_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCES_NAME = "Recipe Preferences"

        const val PREFERENCES_BACK_ONLINE = "backOnline"



    }


}