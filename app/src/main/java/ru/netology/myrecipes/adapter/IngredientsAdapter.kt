package ru.netology.myrecipes.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipes.databinding.FragmentBasketBinding
import androidx.recyclerview.widget.ListAdapter
import kotlinx.android.synthetic.main.recipes_list.view.*
import ru.netology.myrecipes.data.Recipe

class IngredientsAdapter(

    private val interactionListener: IngredientsInteractionListener

) : ListAdapter<String, IngredientsAdapter.IngredientsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        Log.d("IngredientsAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentBasketBinding.inflate(inflater, parent, false)
        return IngredientsViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        Log.d("IngredientsAdapter", "onBindViewHolder:$position")
        holder.bind(getItem(position))
    }

    override fun submitList(ingredients: MutableList<String>?) {
        super.submitList(ingredients.let {ArrayList(it)})
    }

    class IngredientsViewHolder(
        private val binding: FragmentBasketBinding,
        listener: IngredientsInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var ingredient: String
        private lateinit var recipe: Recipe
        
//        init {
//            binding.deleteIngredientMaterialButton.setOnClickListener { listener.onDeleteIngredientsClicked(ingredient)
//                binding.basketButton.setOnClickListener { listener.onBasketClicked(ingredient) }
//                binding.addBasketButton.setOnClickListener { listener.onAddBasketButtonClicked(ingredient) }
//            }
//        }

        fun bind(ingredient: String) {
            this.ingredient = ingredient
            with(binding) {
                ingredientsText.visibility = View.VISIBLE
                ingredientsList.toString()
//                deleteIngredientMaterialButton.visibility = View.VISIBLE
                empty.visibility = View.VISIBLE
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem
        }
    }
}