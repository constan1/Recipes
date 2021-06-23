package com.example.recipes.data.adapters

import android.app.ProgressDialog.show
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.data.database.entities.FavoritesEntity
import com.example.recipes.databinding.FavoriteRecipesRowLayoutBinding
import com.example.recipes.ui.fragments.favourites.FavoriteRecipesFragmentDirections
import com.example.recipes.util.RecipesDiffUtil
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter (
        private val requireActivity: FragmentActivity,
        private val mainViewModel:MainViewModel


): RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(),ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode

    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteRecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(favoritesEntity: FavoritesEntity)
        {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
       return favoriteRecipes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
      val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single Click Listener
         */

        holder.itemView.favoriterecipeRowLayout.setOnClickListener {

            if(multiSelection)
            {
                applySelection(holder,currentRecipe)
            }
            else
            {
                val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(currentRecipe.result)
                holder.itemView.findNavController().navigate((action))
            }
        }



        /**
         * Long Click Listener
         */
        holder.itemView.favoriterecipeRowLayout.setOnLongClickListener {

            if(!multiSelection)
            {
                multiSelection = true
                requireActivity.startActionMode(this)

                applySelection(holder,currentRecipe)
                true


            }
            else
            {
                multiSelection = false
                 false
            }

        }


    }

    private fun applySelection(holder: MyViewHolder, currentRecipe:FavoritesEntity)
    {
        if(selectedRecipes.contains(currentRecipe))
        {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.darkGrey)
            applyActionModeTitle()
        }
        else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int)
    {

            holder.itemView.favoriterecipeRowLayout.setBackgroundColor(
                    ContextCompat.getColor(requireActivity, backgroundColor)
            )
        holder.itemView.favorite_row_cardView.strokeColor =

                ContextCompat.getColor(requireActivity,strokeColor)
    }
    private fun applyActionModeTitle()
            {
                when(selectedRecipes.size)
                {
                    0 -> {
                        mActionMode.finish()
                    }
                    1 -> {
                        mActionMode.title = "${selectedRecipes.size} item selected"
                    }
                    else ->
                    {
                        mActionMode.title = "${selectedRecipes.size} items selected"

                    }
                }
            }


    override fun onActionItemClicked(mode: ActionMode?, menu: MenuItem?): Boolean {


        if(menu?.itemId==R.id.delete_favorite_recipe_menu)
        {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s Removed")

            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu,menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contexttualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)

    }

    private fun applyStatusBarColor(color:Int){

        requireActivity.window.statusBarColor =
                ContextCompat.getColor(requireActivity,color)

    }


    fun setData(newFavoriteRecipes: List<FavoritesEntity>)
    {
        val favoriteRecipesDiffUtil =
                RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)

    }

    private fun showSnackBar(message: String)
    {
        Snackbar.make(
                rootView,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("OK"){}
                .show()
    }

    fun clearContextualActionMode()
    {
        if(this::mActionMode.isInitialized)
        {
            mActionMode.finish()
        }
    }
}