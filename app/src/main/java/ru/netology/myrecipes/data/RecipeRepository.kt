package ru.netology.myrecipes.data

import androidx.lifecycle.LiveData

interface RecipeRepository {
    val data: LiveData<List<Recipe>>

    fun like(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun update(recipe: Recipe)
    fun insert(recipe: Recipe)
    fun search(recipeName: String)
//    fun getCategory(categoryId: Int)

//    fun filterByCategory(categoryId: Int): Boolean


    companion object {
        const val NEW_RECIPE_ID = 0L
    }
}