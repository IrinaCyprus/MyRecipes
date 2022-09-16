package ru.netology.myrecipes.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.netology.myrecipes.adapter.IngredientsInteractionListener
import ru.netology.myrecipes.adapter.RecipeInteractionListener
import ru.netology.myrecipes.data.Categories
import ru.netology.myrecipes.data.NewFileRecipeRepository
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.data.RecipeRepository
import ru.netology.myrecipes.util.SingleLiveEvent
import java.util.*

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application),
    RecipeInteractionListener,
    IngredientsInteractionListener {

    @OptIn(DelicateCoroutinesApi::class)

    private val repository: RecipeRepository = NewFileRecipeRepository(application)

    //        RecipeRepositoryImpl(
//            dao = AppDb.getInstance(
//                context = application
//            ).postDao
//        )

    val data: LiveData<List<Recipe>> = repository.data as MutableLiveData<List<Recipe>>
    val currentRecipe = MutableLiveData<Recipe?>(null)
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe?>()
    val navigatetoIngredientsContent = SingleLiveEvent<String?>()
    val ingredientsList = MutableLiveData<String?>(null)

    //    val ingredientsList = SingleLiveEvent<String>()
    val imageContent = MutableLiveData<String?>(null)
    val openRecipeContent = SingleLiveEvent<Long>()
    private val categoryList = mutableSetOf<Int>()

    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id_recipe)
    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id_recipe)
    override fun onEditeClicked(recipe: Recipe) {
//        repository.update(recipe)
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe
    }

    override fun onEuropeanClicked(categoryId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun onAsianClicked(categoryId: Int) {
        TODO("Not yet implemented")
    }

    override fun onRussianClicked(categoryId: Int) {
        TODO("Not yet implemented")
    }

    override fun onAddImage(recipe: Recipe) {
        val url: String = requireNotNull(recipe.head_image)
        imageContent.value = url
    }

    fun onAddClicked() {
        navigateToRecipeContentScreenEvent.call()
    }

    override fun onAddBasketButtonClicked(recipe: Recipe) {
        ingredientsList.value = recipe.ingredients
    }

    override fun onOpenRecipeClicked(recipe: Recipe) {
        openRecipeContent.value = recipe.id_recipe
    }

    override fun onBasketClicked(recipe: Recipe) {
        navigatetoIngredientsContent.call()
    }

    override fun onCategoryClicked(categoryId: Int): Boolean = categoryList.add(categoryId)

    fun getCategoryById(): Int {
        return categoryList.size
    }

    fun searchRecipeByName(nameRecipe: String) = repository.search(nameRecipe)

    fun onSaveButtonClicked(
        nameRecipe: String,
        imageContent: String,
        categoryId: Int,
        category: String,
        ingredients: String,
        content: String
    ) {
        if (nameRecipe.isBlank()) return
        if (category.isBlank()) return
        if (imageContent.isBlank()) return
        if (ingredients.isBlank()) return
        if (content.isBlank()) return
        val recipe = currentRecipe.value?.copy(
            name_recipe = nameRecipe,
            category = category,
            categoryId = categoryId,
            head_image = imageContent,
            ingredients = ingredients,
            content = content
        ) ?: Recipe(
            id_recipe = RecipeRepository.NEW_RECIPE_ID,
            head_image = imageContent,
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
//        imageContent.value = null
    }

    override fun onDeleteIngredientsClicked(ingredient: String) {
        TODO("Not yet implemented")
    }

}