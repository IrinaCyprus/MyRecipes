package ru.netology.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentFavoritesBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val adapter = RecipeAdapter(viewModel)
        binding.recipesFavoritesList.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes.filter { it.likedByMe })
            binding.empty2.isVisible = recipes.none { it.likedByMe }
        }

        viewModel.openRecipeContent.observe(viewLifecycleOwner) { id ->
            findNavController().navigate(
                R.id.action_favoritesFragment_to_recipeFragment,
                Bundle().apply {
                    putLong(FeedFragment.KEY_ID, id)
                })
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
        return binding.root
    }
}