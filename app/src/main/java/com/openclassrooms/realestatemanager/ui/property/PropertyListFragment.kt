package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.RealEstatesListFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
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
//        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED // todo to disable the swipe from the container to get pressback
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            PropertyListOnBackPressedCallback(slidingPaneLayout)
        )

        val adapter = PropertyAdapter {
            viewModel.updateSelectedProperty(it)
            binding.slidingPaneLayout.openPane()
        }
        binding.realEstateRecyclerView.adapter = adapter
        viewModel.propertiesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}