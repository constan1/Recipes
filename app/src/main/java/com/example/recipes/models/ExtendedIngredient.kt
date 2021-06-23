package com.example.recipes.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ExtendedIngredient(
    val aisle: String?,
    val amount: Double?,
    val consistency: String?,
    val image: String?,
    val name: String?,
    val original: String?,
    val unit: String?
):Parcelable