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

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.filter)
            .setView(view)
            .setPositiveButton(R.string.apply_filter) { dialog, which ->
                applyFilter(binding)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
//        todo add resetButton

        return builder.create()
    }

    private fun applyFilter(binding: FilterDialogFragmentBinding) {
        val minPrice = binding.minPrice.text.toString().toIntOrNull() ?: 0
        val maxPrice = binding.maxPrice.text.toString().toIntOrNull() ?: Int.MAX_VALUE
        viewModel.setPriceRange(minPrice, maxPrice)
    }

    private fun setFilter(binding: FilterDialogFragmentBinding) {
        viewModel.minPrice.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.minPrice.setText(it.toString())
            }
        }
        viewModel.maxPrice.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.maxPrice.setText(it.toString())
            }
        }
    }
}