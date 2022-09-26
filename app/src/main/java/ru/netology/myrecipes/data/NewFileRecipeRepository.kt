package ru.netology.myrecipes.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NewFileRecipeRepository(private val application: Application) : RecipeRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type
    private val idPref = application.getSharedPreferences("id", Context.MODE_PRIVATE)
    private var nextId = 0L

    override val data: MutableLiveData<List<Recipe>>

    init {
        nextId = idPref.getLong(NEXT_ID_PREFS_KEY, 0)
        val postsId = application.filesDir.resolve(FILE_NAME)
        val recipes: List<Recipe> = if (postsId.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it, type) }
        } else emptyList()
        data = MutableLiveData(recipes)
    }

    private var recipes
        get() = checkNotNull(data.value)
        set(value) {
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            )
                .bufferedWriter()
                .use {
                    it.write(gson.toJson(value))
                }
            data.value = value
        }

    override fun like(recipeId: Long) {
        recipes =
            recipes.map {
                if (it.id_recipe == recipeId) {
                    it.copy(likedByMe = !it.likedByMe)
                } else {
                    it
                }
            }
        data.value = recipes
    }

    override fun delete(recipeId: Long) {
        recipes = recipes.filterNot { it.id_recipe == recipeId }
    }

    override fun save(recipe: Recipe) {
        if (recipe.id_recipe == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else update(recipe)
    }

    override fun update(recipe: Recipe) {
        recipes = recipes.map {
            if (it.id_recipe == recipe.id_recipe) recipe else it
        }
    }

    override fun insert(recipe: Recipe) {
        recipes = listOf(
            recipe.copy(id_recipe = ++nextId)
        ) + recipes
    }

    override fun search(recipeName: String) {
        recipes.find {
            it.name_recipe == recipeName
        } ?: throw RuntimeException("empty")
        data.value = recipes
    }

    override fun getFilteredList(filters: MutableSet<String>?): LiveData<List<Recipe>> {
        if (filters.isNullOrEmpty()) {
            return data
        }
        val filteredRecipe = data.map { recipeList ->
            val newRecipes = recipeList.filter {
                it.category in filters
            }
            newRecipes
        }
        return filteredRecipe
    }

    private companion object {
        const val NEXT_ID_PREFS_KEY = "id_post"
        const val FILE_NAME = "posts.json"
    }
}
