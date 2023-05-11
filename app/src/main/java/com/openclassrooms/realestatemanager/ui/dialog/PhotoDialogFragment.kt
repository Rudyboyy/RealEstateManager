package com.openclassrooms.realestatemanager.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoSliderPagerAdapter
import com.openclassrooms.realestatemanager.utils.ZoomOutPageTransformer

class PhotoDialogFragment : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pager = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.photo_dialog_fragment,
                ViewPager2(requireContext()),
                false
            ) as ViewPager2

        val imageUrls = arguments?.getStringArray("photoUrls") ?: emptyArray()
        val description = arguments?.getStringArray("description") ?: emptyArray()
        val position = arguments?.getInt("position")

        pager.adapter = PhotoSliderPagerAdapter(imageUrls, description)
        pager.setPageTransformer(ZoomOutPageTransformer())
        if (position != null) {
            pager.setCurrentItem(position, false)
        }

        return AlertDialog.Builder(requireContext())
            .setView(pager)
            .create()
    }
}