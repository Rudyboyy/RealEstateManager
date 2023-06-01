package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateItemBinding
import com.openclassrooms.realestatemanager.model.Property
import java.text.NumberFormat
import java.util.*

class PropertyAdapter(
    private val onItemClicked: (Property) -> Unit,
    private val context: Context
    ) :
    ListAdapter<Property, PropertyAdapter.ViewHolder>(RealEstatesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RealEstateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), context
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(property)
        }
        holder.bind(property)
    }

    inner class ViewHolder(private val binding: RealEstateItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Property) {
            binding.propertyType.text = item.type
            binding.propertyPrice.text = context.getString(R.string.price_format_dollar, formatAmount(item.price))
            binding.propertyLocation.text = item.city
            if (item.photos.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(item.photos[0].uri)
                    .into(binding.propertyImage)
            }
        }
    }

    private fun formatAmount(amount: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(amount)
    }

    object RealEstatesDiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean =
            oldItem == newItem
    }
}