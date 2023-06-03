package com.openclassrooms.realestatemanager.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FilterDialogFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class FilterDialogFragment : DialogFragment() {

    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FilterDialogFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        setFilter(binding)

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.filter)
            .setView(view)
            .setPositiveButton(R.string.apply_filter) { dialog, which ->
                applyFilter(binding)
            }
            .setNeutralButton(R.string.reset_filter) { dialog, _ ->
                resetFilter(binding)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    private fun applyFilter(binding: FilterDialogFragmentBinding) {
        val minPrice = binding.minPrice.text.toString().toIntOrNull() ?: 0
        val maxPrice = binding.maxPrice.text.toString().toIntOrNull() ?: Int.MAX_VALUE
        val minSurface = binding.minSurface.text.toString().toIntOrNull() ?: 0
        val maxSurface = binding.maxSurface.text.toString().toIntOrNull() ?: Int.MAX_VALUE
        val minPhoto = binding.numberSpinner.selectedItem?.toString()?.toIntOrNull() ?: 0
        viewModel.setSurfaceRange(minSurface, maxSurface)
        viewModel.setPriceRange(minPrice, maxPrice)
        viewModel.setMinPhoto(minPhoto)
    }

    private fun setFilter(binding: FilterDialogFragmentBinding) {
        val minPrice = viewModel.minPrice.value
        val maxPrice = viewModel.maxPrice.value
        val minSurface = viewModel.minSurface.value
        val maxSurface = viewModel.maxSurface.value
        val minPhoto = viewModel.minPhoto.value

        if (minPrice != null && minPrice != 0) {
            binding.minPrice.setText(minPrice.toString())
        } else {
            binding.minPrice.setText("")
        }

        if (maxPrice != null && maxPrice != Int.MAX_VALUE) {
            binding.maxPrice.setText(maxPrice.toString())
        } else {
            binding.maxPrice.setText("")
        }

        if (minSurface != null && minSurface != 0) {
            binding.minSurface.setText(minSurface.toString())
        } else {
            binding.minSurface.setText("")
        }

        if (maxSurface != null && maxSurface != Int.MAX_VALUE) {
            binding.maxSurface.setText(maxSurface.toString())
        } else {
            binding.maxSurface.setText("")
        }

        if (minPhoto != null) {
            val options = resources.getStringArray(R.array.numbers_array)
            val position = options.indexOf(minPhoto.toString())
            if (position != -1) {
                binding.numberSpinner.setSelection(position)
            }
        } else {
            binding.numberSpinner.setSelection(0)
        }
    }

    private fun resetFilter(binding: FilterDialogFragmentBinding) {
        binding.minPrice.setText("")
        binding.maxPrice.setText("")
        binding.minSurface.setText("")
        binding.maxSurface.setText("")
        binding.numberSpinner.setSelection(0)

        applyFilter(binding)
    }
}