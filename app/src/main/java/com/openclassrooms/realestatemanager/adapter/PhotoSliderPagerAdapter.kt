package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.PhotoDialogItemBinding

class PhotoSliderPagerAdapter(
    private val imageUrls: Array<String>,
    private val description: Array<String>
) :
    RecyclerView.Adapter<PhotoSliderPagerAdapter.ImageSlidePagerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSlidePagerViewHolder {
        val binding =
            PhotoDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSlidePagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSlidePagerViewHolder, position: Int) {
        holder.bind(imageUrls[position], description[position])
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageSlidePagerViewHolder(private val binding: PhotoDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String, description: String) {
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.image)
            binding.description.text = description
        }
    }
}
