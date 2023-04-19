package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.RealEstateItemBinding
import com.openclassrooms.realestatemanager.model.Property

class PropertyAdapter(private val onItemClicked: (Property) -> Unit) :
    ListAdapter<Property, PropertyAdapter.ViewHolder>(RealEstatesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RealEstateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(property)
        }
        holder.bind(property)
    }

    class ViewHolder(private val binding: RealEstateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Property) {
            binding.propertyType.text = item.propertyType
        }
    }

    object RealEstatesDiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean =
            oldItem == newItem
    }
}