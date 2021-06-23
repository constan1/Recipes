package com.example.recipes.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.data.adapters.FavoriteRecipesAdapter
import com.example.recipes.databinding.FragmentFavoriteRecipesBinding
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*
import kotlinx.android.synthetic.main.fragment_recipes.*


@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {


    private val mainViewModel: MainViewModel by viewModels()
    private val myAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentFavoriteRecipesBinding.inflate(inflater,container,false)
         binding.lifecycleOwner  = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter =  myAdapter


        setupRecyclerView(binding.favoriteRecipesRecyclerView)

      setupRecyclerView(binding.favoriteRecipesRecyclerView)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }

        return super .onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView)
    {
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar(){
        Snackbar.make(
                binding.root,
                "All Recipes Removed",
                Snackbar.LENGTH_SHORT
        ). setAction("Okay"){}
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        myAdapter.clearContextualActionMode()
    }
}