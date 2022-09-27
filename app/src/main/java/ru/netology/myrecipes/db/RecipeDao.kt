package ru.netology.myrecipes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query(
        "UPDATE recipes SET " +
                "image = :head_image, " +
                "category = :category, " +
                "nameRecipe = :name_recipe, " +
                "ingredients = :ingredients, " +
                "content = :content " +
                "WHERE id = :id_recipe"
    )
    fun update(
        id_recipe: Long,
        head_image: String,
        category: String,
        name_recipe: String,
        ingredients: String,
        content: String
    )

    @Query(
        """
        UPDATE recipes SET
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun like(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)

    @Query("SELECT * FROM recipes WHERE nameRecipe LIKE '%' || :text || '%'")
    fun search(text: String): LiveData<List<RecipeEntity>>
}