package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.Constants.BASE_URL_STATIC_MAP
import com.openclassrooms.realestatemanager.utils.Constants.DEFAULT_MARKER_TYPE
import com.openclassrooms.realestatemanager.utils.Constants.DEFAULT_ZOOM_AND_SIZE
import com.openclassrooms.realestatemanager.utils.Constants.MAPS_API_KEY
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class PropertyDetailsFragment : Fragment(R.layout.property_details_fragment) {

    private val binding by viewBinding { PropertyDetailsFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedProperty.observe(this.viewLifecycleOwner) {
            binding.address.text = it.address
            binding.propertyType.text = it.type
            updateStaticMap(it)
        }
    }

    private fun updateStaticMap(property: Property) {
        val defaultSettings = "$DEFAULT_ZOOM_AND_SIZE&$DEFAULT_MARKER_TYPE"
        val currentLatLng = "${property.latitude},${property.longitude}"
        val url = "$BASE_URL_STATIC_MAP&center=$currentLatLng&$defaultSettings$currentLatLng&key=$MAPS_API_KEY"
        Glide.with(binding.staticMaps)
            .load(url)
            .error(R.drawable.no_wifi)
            .into(binding.staticMaps)
    }
}