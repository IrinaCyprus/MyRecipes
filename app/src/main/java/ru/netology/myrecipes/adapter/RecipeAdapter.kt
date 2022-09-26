package ru.netology.myrecipes.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipes.R
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.RecipesListBinding

internal class RecipeAdapter(

    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RecipeAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipesListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RecipeAdapter", "onBindViewHolder:$position")
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipesListBinding,
        listener: RecipeInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe
        private lateinit var category:Recipe
        private lateinit var ingredients:String

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menuRecipe).apply {
                inflate(R.menu.option_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditeClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.image.setOnClickListener { listener.onOpenRecipeClicked(recipe) }
            binding.like.setOnClickListener { listener.onLikeClicked(recipe) }
//            binding.containedButtonCategory.setOnClickListener { listener.onCategoryClicked(categoryId = 2) }
            binding.basketButton.setOnClickListener { listener.onBasketClicked(ingredients) }
            binding.addBasketButton.setOnClickListener { listener.onAddBasketButtonClicked(recipe) }
            binding.menuRecipe.setOnClickListener { popupMenu.show() }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
//                image.isVisible = recipe.head_image.isNotBlank()
                image.setImageURI(Uri.parse(recipe.head_image))
                authorName.text = recipe.author_name
                containedButtonCategory.text = recipe.category
                nameRecipe.text = recipe.name_recipe
                ingredients.text = recipe.ingredients
                textAddToShoppingList.text = recipe.text_add_to_shopping_list
                textIngredients.text = recipe.text_ingredients
                content.text = recipe.content
                like.isChecked = recipe.likedByMe
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id_recipe == newItem.id_recipe

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}