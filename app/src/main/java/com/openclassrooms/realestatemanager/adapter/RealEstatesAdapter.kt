package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.RealEstateItemBinding
import com.openclassrooms.realestatemanager.model.RealEstate

class RealEstatesAdapter :
    ListAdapter<RealEstate, RealEstatesAdapter.ViewHolder>(RealEstatesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RealEstateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: RealEstateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RealEstate) {
            binding.realEstateItemTextView.text = item.propertyType
        }
    }

    object RealEstatesDiffCallback : DiffUtil.ItemCallback<RealEstate>() {
        override fun areItemsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean =
            oldItem == newItem
    }
}