package com.openclassrooms.realestatemanager.ui.property

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.Constants.BASE_URL_STATIC_MAP
import com.openclassrooms.realestatemanager.utils.Constants.DEFAULT_MARKER_TYPE
import com.openclassrooms.realestatemanager.utils.Constants.DEFAULT_ZOOM_AND_SIZE
import com.openclassrooms.realestatemanager.utils.Constants.MAPS_API_KEY
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PropertyDetailsFragment : Fragment(R.layout.property_details_fragment) {

    private val binding by viewBinding { PropertyDetailsFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val priceFormatResId =
            if (false) R.string.price_format_eur else R.string.price_format_dollar
        //todo change false to a val currentCurrency: Boolean to get the current currency
        val priceFormat = getString(priceFormatResId)
        val surfaceFormat = getString(R.string.square_meters)
        viewModel.selectedProperty.observe(this.viewLifecycleOwner) {
            binding.address.text = it.address
            binding.propertyType.text = it.type
            binding.pointOfInterest.text = it.pointOfInterest
            binding.propertyPrice.text = switchDoubleToInt(priceFormat, it.price)
            binding.agent.text = it.agent
            binding.bathroom.text = it.numberOfBathrooms.toString()
            binding.bedroom.text = it.numberOfBedrooms.toString()
            binding.propertyRooms.text = it.numberOfRooms.toString()
            binding.creationDate.text = getFormattedDate(it.entryDate)
            binding.sellDate.text = it.saleDate?.let { date -> getFormattedDate(date) }
            binding.propertyDescription.text = it.description
            binding.surface.text = switchDoubleToInt(surfaceFormat, it.surface)
            binding.propertyStatus.text = it.status.name
            // To go back to top when the view is updated
            binding.nestedScrollView.scrollTo(0, 0)
            initRecyclerView(it.photos)
            updateStaticMap(it)
            setEditButton(it)
        }
    }

    private fun switchDoubleToInt(format: String, double: Double): String {
        val isInteger = double.toInt().toDouble() == double
        val formattedPrice = if (isInteger) {
            String.format(format, double.toInt())
        } else {
            String.format(format, double)
        }
        return formattedPrice
    }

    private fun initRecyclerView(photos: List<Photo>) {
        val adapter = PhotoAdapter { position ->
            val photoUris = photos.map { it.uri }
            val photoDescriptions = photos.map { it.description }
            val action =
                PropertyListFragmentDirections.actionPropertyListFragmentToImageSlideDialogFragment(
                    photoUris.toTypedArray(),
                    photoDescriptions.toTypedArray(),
                    position
                )
            findNavController().navigate(action)
        }
        binding.imageRecyclerview.adapter = adapter
        adapter.submitList(photos)
    }


    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(date: Date): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(date)
    }


    private fun updateStaticMap(property: Property) {
        val defaultSettings = "$DEFAULT_ZOOM_AND_SIZE&$DEFAULT_MARKER_TYPE"
        val currentLatLng = "${property.latitude},${property.longitude}"
        val url =
            "$BASE_URL_STATIC_MAP&center=$currentLatLng&$defaultSettings$currentLatLng&key=$MAPS_API_KEY"
        Glide.with(binding.staticMaps)
            .load(url)
            .error(R.drawable.no_wifi)
            .into(binding.staticMaps)
    }

    private fun setEditButton(property: Property) {
        binding.editButton.setOnClickListener {
            val action =
            PropertyListFragmentDirections.actionGlobalToAddFragment(property)
            findNavController().navigate(action) }

    }
}