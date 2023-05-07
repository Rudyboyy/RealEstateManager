package com.openclassrooms.realestatemanager.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PhotoDialogFragmentBinding
import com.openclassrooms.realestatemanager.databinding.PhotoDialogItemBinding
import com.openclassrooms.realestatemanager.databinding.PhotoItemBinding
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsFragmentBinding
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.ui.property.PropertyDetailsFragment
import com.openclassrooms.realestatemanager.utils.viewBinding

class PhotoDialogFragment(
    private val photos: List<Photo>,
    private val position: Int
) : DialogFragment(R.layout.photo_dialog_fragment) {

    private val binding by viewBinding { PhotoDialogFragmentBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PhotoPagerAdapter(photos)
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = position
    }

    inner class PhotoPagerAdapter(private val photos: List<Photo>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding = PhotoDialogItemBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
            val view = binding.root
            val photo = photos[position]
            binding.photoDescription.text = photo.description
            Glide.with(binding.photo)
                .load(photo.uri)
                .into(binding.photo)
            container.addView(view)
            return view
        }

        override fun getCount(): Int = photos.size

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}