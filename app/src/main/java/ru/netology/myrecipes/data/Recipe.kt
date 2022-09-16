package ru.netology.myrecipes.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize

data class Recipe(
    val id_recipe: Long,
    val head_image: String,
    val author_name: String,
    val categoryId: Int,
    val category:String,

    val name_recipe:String,
    val text_ingredients: String,
    val text_add_to_shopping_list:String,

    val ingredients: String,
    val content:String,

    val likedByMe:Boolean = false
    ) : Parcelable

@Serializable
@Parcelize
enum class Categories: Parcelable {
    European,
    Russian,
    Asian
}