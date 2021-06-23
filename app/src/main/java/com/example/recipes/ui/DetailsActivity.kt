package com.example.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.example.recipes.R
import com.example.recipes.data.adapters.PagerAdapter
import com.example.recipes.data.database.entities.FavoritesEntity
import com.example.recipes.ui.fragments.ingredients.IngredientsFragment
import com.example.recipes.ui.fragments.instructions.InstructionsFragment
import com.example.recipes.ui.fragments.overview.OverviewFragment
import com.example.recipes.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()

        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val resultBundle = Bundle()

        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            title,
            supportFragmentManager
        )
        viewPager.adapter = adapter
        tablLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, Observer {
        favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.red)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }

                }
            } catch(e: Exception) {
                    Log.d("DetailsActivity", e.message.toString())
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
        {
        finish()
        }
        else if(item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved)
        {
            removeFromFavorites(item)
        }
        return super .onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
                FavoritesEntity(
                      0,
                        args.result
                )

        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.red)
        showSnackBar("Recipe Saved.")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem)
    {
        val favoritesEntity = 
                FavoritesEntity (
                        savedRecipeId,
                        args.result
                )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed From Favorites.")
        recipeSaved = false

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
                detailsLayout,
                message,
                Snackbar.LENGTH_SHORT
        ). setAction("Okay")
        {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {

        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}