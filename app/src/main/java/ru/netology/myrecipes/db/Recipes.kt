package ru.netology.myrecipes.db

import ru.netology.myrecipes.data.Recipe

internal fun RecipeEntity.toModel() = Recipe(
    id_recipe = id_recipe,
    head_image = head_image,
    author_name = author_name,
    categoryId = categoryId,
    category = category,
    name_recipe = name_recipe,
    text_ingredients = text_ingredients,
    text_add_to_shopping_list = text_add_to_shopping_list,
    ingredients = ingredients,
    content = content,
    likedByMe = likedByMe
)
internal fun Recipe.toEntity() = RecipeEntity(
    id_recipe = id_recipe,
    head_image = head_image,
    author_name = author_name,
    categoryId = categoryId,
    category = category,
    name_recipe = name_recipe,
    text_ingredients = text_ingredients,
    text_add_to_shopping_list = text_add_to_shopping_list,
    ingredients = ingredients,
    content = content,
    likedByMe = likedByMe
)