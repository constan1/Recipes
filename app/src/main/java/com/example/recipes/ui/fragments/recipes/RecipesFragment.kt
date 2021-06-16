package com.example.recipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.viewmodels.MainViewModel
import com.example.recipes.R
import com.example.recipes.data.adapters.RecipeAdapter
import com.example.recipes.databinding.FragmentRecipesBinding
import com.example.recipes.util.NetworkListener
import com.example.recipes.util.NetworkResult
import com.example.recipes.util.observeOnce

import com.example.recipes.viewmodels.RecipesViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()

    private  var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy {RecipeAdapter()}

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         _binding = FragmentRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })


       lifecycleScope.launch {
           networkListener = NetworkListener()
           networkListener.checkNetworkAvailability(requireContext())
                   .collect { status ->
                       Log.d("NetworkListener", status.toString())
                        recipesViewModel.networkStatus = status
                       recipesViewModel.showNetworkStatus()
                       readDatabase()

                   }

       }
        binding.recipeFab.setOnClickListener {
            if ( recipesViewModel.networkStatus)
            {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            }
            else {
                recipesViewModel.showNetworkStatus()
            }

        }

        return binding.root

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
       return true
    }

    private fun setupRecyclerView(){
        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).adapter = mAdapter
        binding.root.findViewById<ShimmerRecyclerView>(R.id.recyclerview).layoutManager =
                LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    private fun readDatabase() {

        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, Observer{ database ->
                if (database.isNotEmpty() && !args.BackFromBottomSheet)
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

     private  fun requestApiData() {
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

    private fun searchApiData(searchQuery: String)
    {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipeResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(),
                    Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

     private fun loadDataFromCache()
    {
        lifecycleScope.launch{
            mainViewModel.readRecipe.observe(viewLifecycleOwner, { database ->
                if(database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
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