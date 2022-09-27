package ru.netology.myrecipes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
class RecipeEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id_recipe: Long,

    @ColumnInfo(name = "image")
    val head_image: String,

    @ColumnInfo(name = "author")
    val author_name: String,

    @ColumnInfo(name = "categoryId")
    val categoryId: Int,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name="nameRecipe")
    val name_recipe: String,

    @ColumnInfo(name="text_ingredients")
    val text_ingredients: String,

    @ColumnInfo(name="text_add_to_shopping_list")
    val text_add_to_shopping_list: String,

    @ColumnInfo(name="ingredients")
    val ingredients: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean
)