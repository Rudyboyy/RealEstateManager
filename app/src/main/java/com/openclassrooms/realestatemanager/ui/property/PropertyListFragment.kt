package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.RealEstatesListFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class PropertyListFragment : Fragment(R.layout.real_estates_list_fragment) {

    companion object {
        fun newInstance() = PropertyListFragment()
    }

    private val binding by viewBinding { RealEstatesListFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by viewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PropertyAdapter()
        binding.realEstateRecyclerView.adapter = adapter
        viewModel.allProperties.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}