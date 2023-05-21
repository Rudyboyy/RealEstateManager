package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.openclassrooms.realestatemanager.R

class CheckboxAdapter(private val checkboxList: List<String>) :
    RecyclerView.Adapter<CheckboxAdapter.ViewHolder>() {

    val checkedItems = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.checkbox_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkboxText = checkboxList[position]
        holder.checkbox.text = checkboxText
        holder.checkbox.isChecked = checkedItems.contains(checkboxText)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedItems.add(checkboxText)
            } else {
                checkedItems.remove(checkboxText)
            }
        }
    }

    override fun getItemCount(): Int {
        return checkboxList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)
    }
}
