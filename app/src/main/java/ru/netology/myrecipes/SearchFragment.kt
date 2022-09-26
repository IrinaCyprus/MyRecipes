package ru.netology.myrecipes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentFeedBinding
import ru.netology.myrecipes.databinding.FragmentSearchBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

//class SearchFragment: Fragment(R.layout.fragment_search) {
//
//    val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val binding = FragmentSearchBinding.inflate(inflater, container, false)
//        val adapter = RecipeAdapter(viewModel)
//        binding.recipeRecyclerView.adapter = adapter
//
//        viewModel.data.observe(viewLifecycleOwner) { recipes ->
//            adapter.submitList(recipes)
//        }
//
//        binding.search.setOnQueryTextListener(object :
//            android.widget.SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrEmpty()) {
//                    adapter.submitList(viewModel.data.value)
//                    return true
//                }
//                var recipeList = adapter.currentList
//                recipeList = recipeList.filter { recipe ->
//                    recipe.name_recipe.lowercase().contains(newText.lowercase())
//                }
//                viewModel.searchRecipeByName(newText)
//
//                if (recipeList.isEmpty()) {
//                    Toast.makeText(context, "No recipes", Toast.LENGTH_SHORT).show()
//                    adapter.submitList(recipeList)
//                } else {
//                    adapter.submitList(recipeList)
//                }
//                return true
//            }
//        })
//
//        viewModel.openRecipeContent.observe(viewLifecycleOwner) {
//            binding.search.setQuery("", false)
//            val direction =
//                FeedFragmentDirections.actionFeedFragmentToRecipeFragment()
//            findNavController().navigate(direction)
//        }
//
//        setFragmentResultListener(
//            requestKey = NewRecipeFragment.ADD_REQUEST_KEY
//        ) { requestKey, bundle ->
//            if (requestKey != NewRecipeFragment.ADD_REQUEST_KEY) return@setFragmentResultListener
//            val nameRecipe = bundle.getString(
//                NewRecipeFragment.RESULT_KEY_RECIPE_NAME
//            ) ?: return@setFragmentResultListener
//            val category = bundle.getString(NewRecipeFragment.RESULT_KEY_CATEGORY)
//                ?: return@setFragmentResultListener
//            val ingredients = bundle.getString(NewRecipeFragment.RESULT_KEY_INGREDIENTS)
//                ?: return@setFragmentResultListener
//            val content = bundle.getString(NewRecipeFragment.RESULT_KEY_CONTENT)
//                ?: return@setFragmentResultListener
//            val imageContent = bundle.getString(NewRecipeFragment.RESULT_PHOTO_KEY)
//            if (imageContent != null) {
//                viewModel.onSaveButtonClicked(
//                    nameRecipe = nameRecipe,
//                    category = category,
//                    categoryId = 2,
//                    image = imageContent,
//                    ingredients = ingredients,
//                    content = content
//                )
//            }
//        }
//        return binding.root
//    }
//}