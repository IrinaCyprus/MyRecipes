package ru.netology.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myrecipes.FeedFragment.Companion.KEY_ID
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentRecipeBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

class RecipeFragment : Fragment() {
    lateinit var recipe: Recipe
    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRecipeBinding.inflate(inflater, container, false)

        val viewHolder = RecipeAdapter.ViewHolder(binding.recipeFragment, viewModel)
        val id = arguments?.getLong(KEY_ID)

        viewModel.data.observe(viewLifecycleOwner) { initialRecipe ->
            initialRecipe.firstOrNull { it.id_recipe == id }?.let {
                viewHolder.bind(it)
                return@observe
            }
            findNavController().navigate(R.id.action_feedFragment_to_recipeFragment)
        }

        setFragmentResultListener(
            requestKey = NewRecipeFragment.ADD_REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != NewRecipeFragment.ADD_REQUEST_KEY) return@setFragmentResultListener
            val nameRecipe = bundle.getString(
                NewRecipeFragment.RESULT_KEY_RECIPE_NAME
            ) ?: return@setFragmentResultListener
            val category = bundle.getString(NewRecipeFragment.RESULT_KEY_CATEGORY)
                ?: return@setFragmentResultListener
            val ingredients = bundle.getString(NewRecipeFragment.RESULT_KEY_INGREDIENTS)
                ?: return@setFragmentResultListener
            val content = bundle.getString(NewRecipeFragment.RESULT_KEY_CONTENT)
                ?: return@setFragmentResultListener
            val imageContent = bundle.getString(NewRecipeFragment.RESULT_PHOTO_KEY)
            if (imageContent != null) {
                viewModel.onSaveButtonClicked(
                    nameRecipe = nameRecipe,
                    category = category,
                    categoryId = 2,
//                    image = imageContent,
                    ingredients = ingredients,
                    content = content
                )
            }
        }

        viewModel.ingredientsList.observe(viewLifecycleOwner) {
            val direction =
                RecipeFragmentDirections.actionRecipeFragmentToBasketFragment()
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) {
            val direction = RecipeFragmentDirections.actionRecipeFragmentToNewRecipeFragment()
            findNavController().navigate(direction)
        }
        return binding.root
    }
}

