package com.openclassrooms.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MapFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.FragmentUtils.handleBackPressed
import com.openclassrooms.realestatemanager.utils.Utils.isInternetAvailable
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.MapViewModel
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class MapFragment : Fragment(R.layout.map_fragment), OnMapReadyCallback {

    private val binding by viewBinding { MapFragmentBinding.bind(it) }
    private val mapViewModel by viewModels<MapViewModel>()
    private val realEstateViewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private lateinit var map: GoogleMap
    private val actionFragment: Int = R.id.action_global_to_PropertyListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMap()
        handleBackPressed(actionFragment)
        setBackButton()
        getLocation()
    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (isInternetAvailable(requireContext())) {
            mapFragment?.getMapAsync(this)
            getLocation()
            binding.map.visibility = View.VISIBLE
            binding.noInternetImage.visibility = View.GONE
        } else {
            binding.map.visibility = View.GONE
            binding.noInternetImage.visibility = View.VISIBLE
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = LatLng(it.latitude, it.longitude)
                    mapViewModel.updateUserLocation(userLocation)
                }
            }
    }

    private fun setMarker(result: Property) {
        val marker: Marker? = map.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        result.latitude ?: 0.0,
                        result.longitude ?: 0.0
                    )
                )
                .title(result.type)
                .alpha(0.8f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        if (marker != null) {
            marker.tag = result
            map.setOnInfoWindowClickListener {
                this.onMarkerClick(
                    it
                )
            }
        }
    }

    private fun onMarkerClick(marker: Marker) {
        val tag = marker.tag
        if (tag is Property) {
            val property: Property = tag
            val action =
                MapFragmentDirections.actionGlobalToPropertyListFragment(property)
            findNavController().navigate(action)
        }
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(actionFragment)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        mapViewModel.userLocation.observe(viewLifecycleOwner) { location ->
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
        realEstateViewModel.filteredProperties.observe(viewLifecycleOwner) {
            for (property in it) {
                setMarker(property)
            }
        }
    }
}