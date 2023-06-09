package com.openclassrooms.realestatemanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.PhotoItemBinding
import com.openclassrooms.realestatemanager.model.Photo

class PhotoAdapter(
    private val onItemClicked: (Int) -> Unit,
    private val onItemDeleteClicked: (Int) -> Unit
) :
    ListAdapter<Photo, PhotoAdapter.ViewHolder>(PhotosDiffCallback) {

    private var isInAddFragment: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }
        holder.bind(photo)
    }

    inner class ViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo) {
            binding.photoDescription.text = item.description
            val url = item.uri
            Glide.with(binding.photo)
                .load(url)
                .into(binding.photo)
            if (isInAddFragment) {
                binding.deleteButton.visibility = View.VISIBLE
                binding.deleteButton.setOnClickListener {
                    onItemDeleteClicked(bindingAdapterPosition)
                }
            } else {
                binding.deleteButton.visibility = View.GONE
            }
        }
    }

    object PhotosDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setInAddFragment(inAddFragment: Boolean) {
        isInAddFragment = inAddFragment
        notifyDataSetChanged()
    }

}