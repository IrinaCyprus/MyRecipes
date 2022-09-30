package ru.netology.myrecipes.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipes.R
import ru.netology.myrecipes.data.Recipe
import ru.netology.myrecipes.databinding.FragmentFeedBinding
import ru.netology.myrecipes.databinding.FragmentToFeedBinding
import ru.netology.myrecipes.databinding.RecipesListBinding

internal class RecipesAdapter(

    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentToFeedBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: FragmentToFeedBinding,
        listener: RecipeInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe
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
            binding.menuRecipe.setOnClickListener { popupMenu.show() }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                image.setImageURI(Uri.parse(recipe.head_image))
                authorName.text = recipe.author_name
                containedButtonCategory.text = recipe.category
                nameRecipe.text = recipe.name_recipe
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