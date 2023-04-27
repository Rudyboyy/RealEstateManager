package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class PropertyDetailsFragment : Fragment(R.layout.property_details_fragment) {

    private val binding by viewBinding { PropertyDetailsFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private val latitude = "48.8566"
    private val longitude = "2.3522"
    private val defaultSettings = "${Constants.DEFAULT_ZOOM_AND_SIZE}&${Constants.DEFAULT_MARKER_TYPE}"
    private val currentLatLng = "$latitude,$longitude"
    private val url = "${Constants.BASE_URL_STATIC_MAP}&center=$currentLatLng&$defaultSettings$currentLatLng&key=${BuildConfig.MAPS_API_KEY}"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedProperty.observe(this.viewLifecycleOwner) {
            binding.propertyType.text = it.type
            Glide.with(binding.staticMaps)
                .load(url)
                .error(R.drawable.ic_add_property)
                .into(binding.staticMaps)
        }
    }
}