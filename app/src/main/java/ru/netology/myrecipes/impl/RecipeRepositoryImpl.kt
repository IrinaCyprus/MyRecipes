package ru.netology.myrecipes.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.data.RecipeRepository
import ru.netology.myrecipes.db.RecipeDao
import ru.netology.myrecipes.db.toEntity
import ru.netology.myrecipes.db.toModel

class RecipeRepositoryImpl (private val dao: RecipeDao) : RecipeRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun like(recipeId: Long) {
        dao.like(recipeId)
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }

    override fun save(recipe: Recipe) {
        if (recipe.id_recipe== RecipeRepository.NEW_RECIPE_ID) dao.insert(recipe.toEntity())
        else dao.update(recipe.id_recipe, recipe.head_image, recipe.category, recipe.name_recipe,recipe.ingredients, recipe.content)
    }

    override fun update(recipe: Recipe) {
        dao.update(recipe.id_recipe, recipe.head_image, recipe.category, recipe.name_recipe,recipe.ingredients, recipe.content)
    }

    override fun insert(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun search(recipeName: String) {
        dao.search(recipeName)
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
}