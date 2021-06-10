package com.example.recipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.viewmodels.MainViewModel
import com.example.recipes.R
import com.example.recipes.data.adapters.RecipeAdapter
import com.example.recipes.databinding.FragmentRecipesBinding
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.NetworkResult

import com.example.recipes.viewmodels.RecipesViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private  var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy {RecipeAdapter()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }
    lateinit var recyclerview_main : ShimmerRecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         _binding = FragmentRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        readDatabase()

        return binding.root

    }
    private fun setupRecyclerView(){
        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).adapter = mAdapter
        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).layoutManager =
                LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {

        lifecycleScope.launch {
            mainViewModel.readRecipe.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty())
                {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }

    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(owner = viewLifecycleOwner, onChanged = { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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

    private fun loadDataFromCache()
    {
        lifecycleScope.launch{
            mainViewModel.readRecipe.observe(viewLifecycleOwner, { database ->
                mAdapter.setData(database[0].foodRecipe)
            })
        }

    }


    private fun showShimmerEffect(){

        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).showShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hideShimmerEffect(){
        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).hideShimmer()
    }
}