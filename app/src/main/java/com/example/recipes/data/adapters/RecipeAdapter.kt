package com.example.recipes.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.recipes.data.FoodRecipe
import com.example.recipes.databinding.RecipeRowLayoutBinding
import com.example.recipes.util.RecipesDiffUtil
import com.example.recipes.models.Result

class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    private var currentRecipe = emptyList<Result>()

    class MyViewHolder(private val binding: RecipeRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return currentRecipe.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = currentRecipe[position]
        holder.bind(currentResult)
    }


    fun setData(newData: FoodRecipe)
    {
        var recipeDiffUtil = RecipesDiffUtil(currentRecipe,newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
        currentRecipe = newData.results
        diffUtilResult.dispatchUpdatesTo(this)

    }

}