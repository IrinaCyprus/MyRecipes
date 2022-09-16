package ru.netology.myrecipes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_recipe.*
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentFeedBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel
import java.util.*

class FeedFragment : Fragment(R.layout.fragment_feed) {

    lateinit var binding:FragmentFeedBinding
    val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = RecipeAdapter(viewModel)
        binding.recipeRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        setFragmentResultListener(
            requestKey = NewRecipeFragment.ADD_REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != NewRecipeFragment.ADD_REQUEST_KEY) return@setFragmentResultListener
            val nameRecipe = bundle.getString(
                NewRecipeFragment.RESULT_KEY_RECIPE_NAME
            ) ?: return@setFragmentResultListener
            val category = bundle.getString(NewRecipeFragment.RESULT_KEY_CATEGORY)
                ?:return@setFragmentResultListener
            val ingredients = bundle.getString(NewRecipeFragment.RESULT_KEY_INGREDIENTS)
                ?:return@setFragmentResultListener
            val content = bundle.getString(NewRecipeFragment.RESULT_KEY_CONTENT)
                ?:return@setFragmentResultListener
            val imageContent = bundle.getString(NewRecipeFragment.RESULT_PHOTO_KEY)
            if (imageContent != null) {
                viewModel.onSaveButtonClicked(
                    nameRecipe = nameRecipe,
                    category = category,
                    categoryId = 2,
                    imageContent = imageContent,
                    ingredients = ingredients,
                    content = content
                )
            }
        }

        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) {
            val direction =
                FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment()
            findNavController().navigate(direction)
        }

        viewModel.openRecipeContent.observe(viewLifecycleOwner) {
                       findNavController().navigate(R.id.action_feedFragment_to_recipeFragment,
                Bundle().apply {
                    putLong(KEY_ID, it)
                }
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_category, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.european -> {
                        if (!viewModel.onCategoryClicked(EUROPEAN)) {
                            toastEmptyCategory()
                        }
                        viewModel.getCategoryById()
//                        binding.recipeRecyclerView.setOnClickListener {
//                            viewModel.getCategoryById()
//                        }
                        true
                    }
                    R.id.russian -> {
                        if (!viewModel.onCategoryClicked(RUSSIAN)) {
                            toastEmptyCategory()
                        }
                        true
                    }
                    R.id.asian -> {
                        if (!viewModel.onCategoryClicked(ASIAN)) {
                            toastEmptyCategory()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
    private fun toastEmptyCategory() {
        Toast.makeText(
            requireActivity(),
            getString(R.string.no_recipes),
            Toast.LENGTH_SHORT
        ).show()

    }
    companion object {
        const val KEY_ID = "id"
        const val EUROPEAN = 0
        const val RUSSIAN = 1
        const val ASIAN = 2
    }
}
