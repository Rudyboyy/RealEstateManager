package com.openclassrooms.realestatemanager.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.DialogFragment
import com.openclassrooms.realestatemanager.databinding.PermissionDialogFragmentBinding
import com.openclassrooms.realestatemanager.utils.viewBinding

class PermissionDialogFragment : DialogFragment() {

    private val binding by viewBinding { PermissionDialogFragmentBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setUpSettingsButton()
    }

    private fun setUpSettingsButton() {
        binding.settings.setOnClickListener {
            val settings = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settings)
            dismiss()
        }
    }
}