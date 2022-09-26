package ru.netology.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.ingredient.*
import ru.netology.myrecipes.adapter.IngredientsAdapter
import ru.netology.myrecipes.databinding.FragmentBasketBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

class BasketFragment : Fragment(R.layout.fragment_basket) {

    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentBasketBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val ingredients = resources.getStringArray(R.array.categories).toMutableList()
        val adapterBasket = IngredientsAdapter(viewModel)
        binding.ingredientsList.adapter = adapterBasket

        adapterBasket.submitList(ingredients)

        binding.deleteIngredientMaterialButton.setOnClickListener {
            viewModel.onDeleteIngredientsClicked(ingredient = "")
        }
    }.root
}