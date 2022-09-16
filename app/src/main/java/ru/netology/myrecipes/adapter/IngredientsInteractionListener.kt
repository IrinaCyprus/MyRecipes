package ru.netology.myrecipes.adapter

import ru.netology.myrecipes.data.Recipe

interface IngredientsInteractionListener {
    fun onDeleteIngredientsClicked(ingredient: String)
}