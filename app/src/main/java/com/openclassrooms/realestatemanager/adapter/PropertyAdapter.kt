package com.openclassrooms.realestatemanager.adapter

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateItemBinding
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.NumberFormat
import java.util.*

class PropertyAdapter(
    private val onItemClicked: (Property) -> Unit,
    private val context: Context
) :
    ListAdapter<Property, PropertyAdapter.ViewHolder>(RealEstatesDiffCallback) {

    private var selectedItemIndex: Int = RecyclerView.NO_POSITION
    private var isCurrencyDollar: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RealEstateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), context
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(property)
            selectedItemIndex = holder.absoluteAdapterPosition
            notifyDataSetChanged()
        }
        setSelectedBackgroundColor(position, holder)
        holder.bind(property)
    }

    private fun setSelectedBackgroundColor(position: Int, holder: ViewHolder) {
        val isSelected = selectedItemIndex == position
        val backgroundColor = if (isSelected) {
            ContextCompat.getColor(context, R.color.colorAccent)
        } else {
            val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val currentMode = uiModeManager.nightMode

            if (currentMode == UiModeManager.MODE_NIGHT_NO) {
                Color.WHITE
            } else {
                Color.DKGRAY
            }
        }
        holder.itemView.setBackgroundColor(backgroundColor)
    }

    inner class ViewHolder(
        private val binding: RealEstateItemBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Property) {
            var price = item.price
            var priceFormat = R.string.price_format_dollar
            if (!isCurrencyDollar) {
                price = Utils.convertDollarToEuro(item.price.toInt()).toDouble()
                priceFormat = R.string.price_format_eur
            }
            binding.propertyType.text = item.type
            binding.propertyPrice.text =
                context.getString(priceFormat, formatAmount(price))
            binding.propertyLocation.text = item.city
            if (item.photos.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(item.photos[0].uri)
                    .into(binding.propertyImage)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrency(isDollar: Boolean) {
        isCurrencyDollar = isDollar
        notifyDataSetChanged()
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