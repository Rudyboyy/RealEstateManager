package com.openclassrooms.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MapFragmentBinding
import com.openclassrooms.realestatemanager.utils.FragmentUtils.handleBackPressed
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.MapViewModel

class MapFragment : Fragment(R.layout.map_fragment), OnMapReadyCallback {

    private val binding by viewBinding { MapFragmentBinding.bind(it) }
    private val viewModel by viewModels<MapViewModel>()
    private lateinit var map: GoogleMap
    private val actionFragment: Int = R.id.action_MapFragment_to_PropertyListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        handleBackPressed(actionFragment)
        setBackButton()
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = LatLng(it.latitude, it.longitude)
                    viewModel.updateUserLocation(userLocation)
                }
            }
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(actionFragment)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        viewModel.userLocation.observe(viewLifecycleOwner) { location ->
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}