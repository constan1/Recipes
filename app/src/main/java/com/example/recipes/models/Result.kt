package com.example.recipes.models

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Result(
    val aggregateLikes: Int,
    val cheap: Boolean,

    val dairyFree: Boolean,

    val extendedIngredients: @RawValue List<ExtendedIngredient?>?,

    val glutenFree: Boolean,

    val id: Int,
    val image: String,


    val readyInMinutes: Int,

    val sourceName: String?,
    val sourceUrl: String,

    val summary: String,

    val title: String,

    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean

):Parcelable