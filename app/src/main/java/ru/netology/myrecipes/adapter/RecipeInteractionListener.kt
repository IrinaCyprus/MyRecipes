package ru.netology.myrecipes.adapter

import android.net.Uri
import ru.netology.myrecipes.data.Recipe
import java.util.*

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditeClicked(recipe: Recipe)

//    fun onEuropeanClicked(categoryId: Int): Int
//    fun onAsianClicked(categoryId: Int)
//    fun onRussianClicked(categoryId: Int)

    fun onOpenRecipeClicked(recipe: Recipe)
//    fun onCategoryClicked(categoryId: Int): Boolean

    fun onBasketClicked(ingredient: String)
    fun onAddBasketButtonClicked(recipe: Recipe)

    fun onAddImage(recipe: Recipe)
}