package ru.netology.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.ingredient.*
import ru.netology.myrecipes.adapter.IngredientsAdapter
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.databinding.FragmentBasketBinding
import ru.netology.myrecipes.databinding.FragmentFavoritesBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

class BasketFragment: Fragment(R.layout.fragment_basket) {
    lateinit var binding: FragmentBasketBinding
    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        val adapter = IngredientsAdapter(viewModel)
        binding.ingredientsList.adapter = adapter

//        viewModel.ingredientsList.observe(viewLifecycleOwner) { ingredients ->
//            adapter.submitList(ingredients)
//        }

        viewModel.data.observe(viewLifecycleOwner) { ingredients ->
//            adapter.submitList(ingredients)
            binding.empty.isVisible = ingredients.isEmpty()
        }
//        binding.deleteIngredientMaterialButton.setOnClickListener { ingredient ->
//            viewModel.onDeleteIngredientsClicked(ingredient)
//        }

        return binding.root
    }

}