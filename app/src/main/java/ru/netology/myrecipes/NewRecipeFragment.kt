package ru.netology.myrecipes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.myrecipes.databinding.FragmentNewRecipeBinding
import ru.netology.myrecipes.viewModel.RecipeViewModel


class NewRecipeFragment : Fragment() {

    lateinit var binding: FragmentNewRecipeBinding
    private var imageContent: Uri? = null
    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewRecipeBinding.inflate(inflater, container, false)

        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            requireActivity().contentResolver.takePersistableUriPermission(
                requireNotNull(it),
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            binding.image.setImageURI(it)
            viewModel.imageContent.value = it.toString()
        }

        binding.saveButton.setOnClickListener {
            onButtonClicked(binding)
        }

        binding.buttonAddImage.setOnClickListener {
            image.launch(arrayOf("image/*"))
        }

        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                with(binding) {
                    addNameRecipe.setText(recipe.name_recipe)
                    category.setSelection(recipe.categoryId)
                    addTextIngredients.setText(recipe.ingredients)
                    viewModel.imageContent.value = recipe.head_image
                    contentEditText.setText(recipe.content)
                }
            }
        }
        return binding.root
    }

    private fun onButtonClicked(binding: FragmentNewRecipeBinding) {
        val nameRecipe = binding.addNameRecipe.text.toString()
        val category = binding.category.selectedItem.toString()
        val ingredients = binding.addTextIngredients.text.toString()
        val content = binding.contentEditText.text.toString()
        if (nameRecipe.isNotBlank()) {
            val resultBundle = Bundle(2)
            resultBundle.putString(RESULT_PHOTO_KEY, imageContent.toString())
            resultBundle.putString(RESULT_KEY_RECIPE_NAME, nameRecipe)
            resultBundle.putString(RESULT_KEY_CATEGORY, category)
            resultBundle.putString(RESULT_KEY_INGREDIENTS, ingredients)
            resultBundle.putString(RESULT_KEY_CONTENT, content)
            setFragmentResult(ADD_REQUEST_KEY, resultBundle)
        }
        findNavController().navigateUp()
    }

    companion object {
        const val ADD_REQUEST_KEY = "addRequestKey"

        //        const val RESULT_KEY = "key"
        const val RESULT_PHOTO_KEY = "photoKey"
        const val RESULT_KEY_RECIPE_NAME = "nameRecipeNewContent"
        const val RESULT_KEY_CATEGORY = "category"
        const val RESULT_KEY_INGREDIENTS = "ingredientsNewContent"
        const val RESULT_KEY_CONTENT = "recipeNewContent"
    }
}
