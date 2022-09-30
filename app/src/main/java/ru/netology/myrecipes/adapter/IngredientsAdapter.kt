package ru.netology.myrecipes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recipes_list.view.*
import ru.netology.myrecipes.databinding.FragmentBasketBinding

class IngredientsAdapter(

    private val interactionListener: IngredientsInteractionListener

) : ListAdapter<String, IngredientsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentBasketBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(ingredients: MutableList<String>?) {
        super.submitList(ingredients.let {ArrayList(it)})
    }

    inner class ViewHolder(
        private val binding: FragmentBasketBinding,
        listener: IngredientsInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var ingredient: String
        
        init {
//            binding.deleteIngredientMaterialButton.setOnClickListener {
//                listener.onDeleteIngredientsClicked(ingredient) }
    }

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