package com.openclassrooms.realestatemanager.utils

import android.content.Context
import com.openclassrooms.realestatemanager.R

object CheckBoxOptionProvider {
    fun getOptions(context: Context): List<String> {
        return listOf(
            context.getString(R.string.school),
            context.getString(R.string.park),
            context.getString(R.string.hospital),
            context.getString(R.string.pharmacy),
            context.getString(R.string.eatery),
            context.getString(R.string.supermarket),
            context.getString(R.string.gas_station),
            context.getString(R.string.shopping_center_mall),
            context.getString(R.string.public_transportation)
        )
    }
}
