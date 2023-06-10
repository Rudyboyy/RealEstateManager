package com.openclassrooms.realestatemanager.ui.property

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.RealEstatesListFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.FragmentUtils.handleBackPressed
import com.openclassrooms.realestatemanager.utils.PropertyListOnBackPressedCallback
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class PropertyListFragment : Fragment(R.layout.real_estates_list_fragment) {

    private val binding by viewBinding { RealEstatesListFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPressed(R.id.PropertyListFragment)
        val slidingPaneLayout = binding.slidingPaneLayout
//        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED // todo to disable the swipe from the container to get press-back
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            PropertyListOnBackPressedCallback(slidingPaneLayout)
        )

        val adapter = PropertyAdapter(
            onItemClicked = {
                viewModel.updateSelectedProperty(it)
                binding.slidingPaneLayout.openPane()
            },
            context = requireContext()
        )
        binding.realEstateRecyclerView.adapter = adapter
        viewModel.filteredProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
                setPropertyFromMap(adapter, it)
            }
        }
        viewModel.currencyLiveData.observe(viewLifecycleOwner) {
            adapter.updateCurrency(it)
        }
    }

    private fun setPropertyFromMap(adapter: PropertyAdapter, propertyList: List<Property>) {
        val property: Property? = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("property", Property::class.java)
        } else {
            arguments?.getParcelable("property")
        }
        if (property != null) {
            binding.slidingPaneLayout.openPane()
            viewModel.updateSelectedProperty(property)
            for (it in propertyList) {
                if (it.id == property.id) {
                    val propertyPosition = adapter.currentList.indexOf(it)
                    if (propertyPosition != -1) {
                        (binding.realEstateRecyclerView.scrollToPosition(propertyPosition))
                    }
                }
            }
        }
    }
}