package com.openclassrooms.realestatemanager.ui.real_estate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.RealEstatesAdapter
import com.openclassrooms.realestatemanager.databinding.RealEstatesListFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.RealEstate
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class RealEstatesListFragment : Fragment(R.layout.real_estates_list_fragment) {

    companion object {
        fun newInstance() = RealEstatesListFragment()
    }

    private val binding by viewBinding { RealEstatesListFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by viewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RealEstatesAdapter()
        binding.realEstateRecyclerView.adapter = adapter
        viewModel.allProperties.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}