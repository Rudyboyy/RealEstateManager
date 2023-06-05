package com.openclassrooms.realestatemanager.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.CheckboxAdapter
import com.openclassrooms.realestatemanager.databinding.FilterDialogFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.utils.CheckBoxOptionProvider
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class FilterDialogFragment : DialogFragment() {

    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private lateinit var checkboxAdapter: CheckboxAdapter
    private val selectedTags = mutableListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FilterDialogFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        initCheckboxRecyclerView(binding)
        initChips(binding)
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
        val poiList = checkboxAdapter.checkedItems
        viewModel.setSurfaceRange(minSurface, maxSurface)
        viewModel.setPriceRange(minPrice, maxPrice)
        viewModel.setMinPhoto(minPhoto)
        viewModel.setPoiList(poiList)
        viewModel.setSortingOption(selectedTags)
        viewModel.getCoordinates("Paris , France").observe(viewLifecycleOwner) {

        }
    }

    private fun setFilter(binding: FilterDialogFragmentBinding) {
        val minPrice = viewModel.minPrice.value
        val maxPrice = viewModel.maxPrice.value
        val minSurface = viewModel.minSurface.value
        val maxSurface = viewModel.maxSurface.value
        val minPhoto = viewModel.minPhoto.value
        val poiList = viewModel.poiList.value
        val chipTags = viewModel.sortingOption.value

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

        if (poiList != null) {
            checkboxAdapter.checkedItems.addAll(poiList)
        }

        if (chipTags != null && chipTags.isNotEmpty()) {
            val chips = listOf(
                binding.chipPriceAsc, binding.chipPriceDesc,
                binding.chipDateAsc, binding.chipDateDesc
            )
            for (chip in chips) {
                val chipTag = chip.tag.toString()
                chip.isChecked = chipTag in chipTags
                if (chip.isChecked) {
                    selectedTags.add(chip.tag as String)
                }
            }
        }
    }

    private fun resetFilter(binding: FilterDialogFragmentBinding) {
        binding.minPrice.setText("")
        binding.maxPrice.setText("")
        binding.minSurface.setText("")
        binding.maxSurface.setText("")
        binding.numberSpinner.setSelection(0)
        checkboxAdapter.checkedItems.clear()
        selectedTags.clear()

        applyFilter(binding)
    }

    private fun initCheckboxRecyclerView(binding: FilterDialogFragmentBinding) {
        val checkboxRecyclerView = binding.checkboxPoi
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnCount = if (screenWidth < 1100) 2 else 3
        val gridLayoutManager = GridLayoutManager(requireContext(), desiredColumnCount)
        checkboxAdapter = CheckboxAdapter(CheckBoxOptionProvider.getOptions(requireContext()))
        checkboxRecyclerView.layoutManager = gridLayoutManager
        checkboxRecyclerView.adapter = checkboxAdapter
    }

    private fun initChips(binding: FilterDialogFragmentBinding) {
        val chipsPairs = listOf(
            binding.chipPriceAsc to binding.chipPriceDesc,
            binding.chipDateAsc to binding.chipDateDesc
        )

        for ((chip, oppositeChip) in chipsPairs) {
            chip.setOnCheckedChangeListener { _, isChecked ->
                val chipTag = chip.tag.toString()
                if (isChecked) {
                    oppositeChip.isChecked = false
                    selectedTags.add(chipTag)
                } else {
                    selectedTags.remove(chipTag)
                }
                Log.v("FILTER_OPTION", selectedTags.toString())
            }

            oppositeChip.setOnCheckedChangeListener { _, isChecked ->
                val chipTag = oppositeChip.tag.toString()
                if (isChecked) {
                    chip.isChecked = false
                    selectedTags.add(chipTag)
                } else {
                    selectedTags.remove(chipTag)
                }
                Log.v("FILTER_OPTION", selectedTags.toString())
            }
        }
    }
}