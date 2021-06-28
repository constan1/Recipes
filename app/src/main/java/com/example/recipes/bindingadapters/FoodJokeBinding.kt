package com.example.recipes.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.models.FoodJoke
import com.example.recipes.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
                view: View,
                ApiResponse:NetworkResult<FoodJoke>?,
                Database: List<FoodJokeEntity>?
        )
        {
            when(ApiResponse) {
                is NetworkResult.Loading -> {
                        when(view)
                        {
                            is ProgressBar -> {
                                view.visibility = View.VISIBLE
                            }
                            is MaterialCardView -> {
                                view.visibility = View.INVISIBLE
                            }
                        }
                }
                is NetworkResult.Error -> {
                    when(view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if(Database != null){
                                view.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                is NetworkResult.Success ->{
                    when(view)
                    {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        @BindingAdapter("readApiResponse4", "readdatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewVisibility(
                view: View,
                apiResponse:NetworkResult<FoodJoke>?,
                database: List<FoodJokeEntity>?
        ) {
           if(database!=null)
           {
               if(database.isEmpty()){
                   view.visibility = View.VISIBLE
                   if(view is TextView){
                       if(apiResponse != null)
                       {
                           view.text = apiResponse.message.toString()
                       }
                   }
               }
           }
            if(apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }
        }
    }
}