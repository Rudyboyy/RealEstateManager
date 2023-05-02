package com.openclassrooms.realestatemanager.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MapFragmentBinding
import com.openclassrooms.realestatemanager.utils.viewBinding

class MapFragment : Fragment(R.layout.map_fragment), OnMapReadyCallback {

    private val binding by viewBinding { MapFragmentBinding.bind(it) }
    private lateinit var map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_PropertyListFragment)//todo a voir
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
    }
}