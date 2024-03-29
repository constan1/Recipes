package com.example.recipes.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipes.R
import com.example.recipes.models.ExtendedIngredient
import com.example.recipes.util.Constants.Companion.BASE_IMAGE_URL
import com.example.recipes.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private  var ingredientsList = emptyList<ExtendedIngredient?>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate
        (R.layout.ingredients_row_layout, parent,false))

    }

    override fun getItemCount(): Int {
       return ingredientsList.size
    }

    //coil is a library to pass data directly into view (xml)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredientsList[position]?.image)
       {
           crossfade(600)
           error(R.drawable.ic_baseline_error_outline_24)

       }
        holder.itemView.ingredient_name.text = ingredientsList[position]?.name?.capitalize()
        holder.itemView.ingredient_amount.text = ingredientsList[position]?.amount?.toString()
        holder.itemView.ingredients_units.text = ingredientsList[position]?.unit
        holder.itemView.ingredient_consistency.text = ingredientsList[position]?.consistency
        holder.itemView.ingredient_original.text = ingredientsList[position]?.original


    }

    fun setData(newingredients: List<ExtendedIngredient?>)
    {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList,newingredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

}