package ru.netology.myrecipes

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search.*
import ru.netology.myrecipes.adapter.RecipeAdapter
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentFeedBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel
import java.util.*

class FeedFragment : Fragment() {
    lateinit var data: LiveData<List<Recipe>>
    private lateinit var adapter: RecipeAdapter
    lateinit var binding: FragmentFeedBinding
    val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
    var list = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        adapter = RecipeAdapter(viewModel)
        binding.recipeRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.filterResult.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNullOrEmpty()) binding.emptyStateGroup.visibility = View.VISIBLE
            else binding.emptyStateGroup.visibility = View.GONE
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.search_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {

                        R.id.search -> {
                            val search = menuItem.actionView as SearchView
                            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    search.clearFocus()

                                    val resultForSubmit = viewModel.filterSearch(query)
                                    adapter.submitList(resultForSubmit)
                                    return false
                                }

                                override fun onQueryTextChange(query: String?): Boolean {
                                    val resultForSubmit = viewModel.filterSearch(query)
                                    adapter.submitList(resultForSubmit)
                                    return true
                                }
                            })
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.absoluteAdapterPosition
                val targetPosition = target.absoluteAdapterPosition
                Collections.swap(list, sourcePosition, targetPosition)
                adapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        })

        touchHelper.attachToRecyclerView(binding.recipeRecyclerView)

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

        viewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) {
            val direction =
                FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment()
            findNavController().navigate(direction)
        }

//TODO не понимаю почему здесь я попадаю не на фрагмент рецепта, а на фрагмент корзины и он еще и лагает
        viewModel.openRecipeContent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_feedFragment_to_recipeFragment,
                Bundle().apply {
                    putLong(KEY_ID, it)
                }
            )
        }
        return binding.root
    }


    companion object {
        const val KEY_ID = "id"
    }
}
