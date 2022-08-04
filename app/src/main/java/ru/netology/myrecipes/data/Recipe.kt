package ru.netology.myrecipes.data

data class Recipe(
    val id_recipe: Int,
    val author_name: String,
    val category:String,
    val head_image: String,
    val ingredients: String,
    val content:String
    )