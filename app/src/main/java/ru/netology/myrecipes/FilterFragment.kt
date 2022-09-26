package ru.netology.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myrecipes.adapter.FilterAdapter
import ru.netology.myrecipes.databinding.FragmentFilterBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel


class FilterFragment: Fragment(R.layout.fragment_filter) {

    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFilterBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val categoriesList = resources.getStringArray(R.array.categories).toMutableList()

        val adapterFilter = FilterAdapter(viewModel)
        binding.filterRecycleView.adapter = adapterFilter

        adapterFilter.submitList(categoriesList)

        binding.selectFilter.setOnClickListener {

            findNavController().popBackStack()
        }
    }.root
}