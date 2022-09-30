package ru.netology.myrecipes.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.netology.myrecipes.adapter.FilterInteractionListener
import ru.netology.myrecipes.adapter.IngredientsInteractionListener
import ru.netology.myrecipes.adapter.RecipeInteractionListener
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.data.RecipeRepository
import ru.netology.myrecipes.db.AppDb
import ru.netology.myrecipes.impl.RecipeRepositoryImpl
import ru.netology.myrecipes.util.SingleLiveEvent
import java.util.*

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application),
    RecipeInteractionListener,
    IngredientsInteractionListener,
    FilterInteractionListener {

    @OptIn(DelicateCoroutinesApi::class)
//  private val repository: RecipeRepository = NewFileRecipeRepository(application)
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )

    val data: LiveData<List<Recipe>> = repository.data as MutableLiveData<List<Recipe>>

    val currentRecipe = MutableLiveData<Recipe?>(null)
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe?>()

    private val ingredientsList = MutableLiveData<String?>(null)

    val imageContent = MutableLiveData<String?>(null)
    val openRecipeContent = SingleLiveEvent<Long>()

    private val filters = MutableLiveData<MutableSet<String>?>(mutableSetOf())
    var filterResult = Transformations.switchMap(filters) { filter ->
        repository.getFilteredList(filter)
    }

    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id_recipe)
    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id_recipe)
    override fun onEditeClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe
        imageContent.value = recipe.toString()
    }

    override fun checkboxFilterPressedOn(category: String) {
        val filterList = filters.value
        filterList?.add(category)
        filters.value = filterList
    }

    override fun checkboxFilterPressedOff(category: String) {
        val filterList = filters.value
        filterList?.remove(category)
        filters.value = filterList
    }

    override fun getStatusCheckBox(category: String): Boolean {
        return filters.value?.contains(category) == true
    }

    override fun onAddImage(recipe: Recipe) {
        val url: String = requireNotNull(recipe.head_image)
        imageContent.value = url
    }

    fun onAddClicked() {
        navigateToRecipeContentScreenEvent.call()
    }

    override fun onAddBasketButtonClicked(ingredient: String) {
        ingredientsList.value = ingredient
    }

    override fun onOpenRecipeClicked(recipe: Recipe) {
        openRecipeContent.value = recipe.id_recipe
    }

    fun onBasketClicked(): MutableLiveData<String?> {
        return ingredientsList
    }

    fun filterSearch(charForSearch: CharSequence?): MutableList<Recipe> {
        val filterRecipes = mutableListOf<Recipe>()
        val recipes = filterResult.value
        if (charForSearch?.isBlank() == true) {
            if (recipes != null) {
                filterRecipes.addAll(recipes)
            }
        } else if (recipes != null) {
            for (recipe in recipes) {
                if (
                    recipe.name_recipe
                        .lowercase(Locale.getDefault())
                        .contains(
                            charForSearch.toString().lowercase(Locale.getDefault())
                        )
                ) {
                    filterRecipes.add(recipe)
                }
            }
        }
        return filterRecipes
    }

    fun onSaveButtonClicked(
        nameRecipe: String,
        categoryId: Int,
        category: String,
        ingredients: String,
        content: String
    ) {
        if (nameRecipe.isBlank()) return
        if (category.isBlank()) return
        if (ingredients.isBlank()) return
        if (content.isBlank()) return
        val recipe = currentRecipe.value?.copy(
            name_recipe = nameRecipe,
            category = category,
            categoryId = categoryId,
            head_image = imageContent.value.toString(),
            ingredients = ingredients,
            content = content
        ) ?: Recipe(
            id_recipe = RecipeRepository.NEW_RECIPE_ID,
            head_image = imageContent.value.toString(),
            author_name = "  Me",
            categoryId = categoryId,
            category = category,
            name_recipe = nameRecipe,
            text_ingredients = "INGREDIENTS:",
            text_add_to_shopping_list = "Add All to Shopping List",
            ingredients = ingredients,
            content = content,
            likedByMe = false
        )
        repository.save(recipe)
        currentRecipe.value = null
        imageContent.value = null
    }

    override fun onDeleteIngredientsClicked(ingredient: String) {
        TODO("Not yet implemented")
    }

}