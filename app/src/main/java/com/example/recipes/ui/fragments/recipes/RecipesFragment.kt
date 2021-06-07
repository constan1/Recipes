package com.example.recipes.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.viewmodels.MainViewModel
import com.example.recipes.R
import com.example.recipes.data.adapters.RecipeAdapter
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.NetworkResult
import com.example.recipes.viewmodels.RecipesViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy {RecipeAdapter()}
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }
    lateinit var recyclerview_main : ShimmerRecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_recipes, container, false)

         mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).showShimmer()


        setupRecyclerView()
        requestApiData()
        return mView
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(owner = viewLifecycleOwner, onChanged = { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }



    private fun setupRecyclerView(){
        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).adapter = mAdapter
        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).layoutManager =
            LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun showShimmerEffect(){

        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).showShimmer()
    }

    private fun hideShimmerEffect(){
        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).hideShimmer()
    }
}