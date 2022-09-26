package ru.netology.myrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myrecipes.databinding.FilterBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel

internal class FilterAdapter(
    private val interactionListener: FilterInteractionListener
) : ListAdapter<String, FilterAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: FilterBinding,
        listener: FilterInteractionListener

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var category: String

        init {
            binding.filterCategoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listener.checkboxFilterPressedOn(category)
                } else listener.checkboxFilterPressedOff(category)
            }
        }

        fun bind(category: String) {
            this.category = category

            with(binding) {
                filterCategoryTextView.text = category
                binding.filterCategoryCheckBox.isChecked =
                    interactionListener.getStatusCheckBox(category)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem


        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}