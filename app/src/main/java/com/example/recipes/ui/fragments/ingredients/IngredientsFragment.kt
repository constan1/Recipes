package com.example.recipes.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.data.adapters.IngredientsAdapter
import com.example.recipes.models.Result
import com.example.recipes.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*


class IngredientsFragment : Fragment() {


    private  val mAdapter: IngredientsAdapter by lazy {IngredientsAdapter()}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle : Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        setUpRecyclerView(view)
        myBundle?.extendedIngredients?.let{mAdapter.setData(it) }

        return view
    }

    private fun setUpRecyclerView(view:View){
        view.ingredients_recyclerview.adapter = mAdapter
        view.ingredients_recyclerview.layoutManager = LinearLayoutManager(requireContext())

    }


}