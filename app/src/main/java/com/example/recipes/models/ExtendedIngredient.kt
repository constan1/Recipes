package com.example.recipes.models

data class ExtendedIngredient(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val image: String,
    val name: String,
    val original: String,
    val unit: String
)