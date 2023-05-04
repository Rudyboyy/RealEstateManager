package com.openclassrooms.realestatemanager.utils

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object FragmentUtils {

    fun Fragment.handleBackPressed(destinationId: Int) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(destinationId)
        }
    }
}