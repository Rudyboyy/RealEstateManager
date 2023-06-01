package com.openclassrooms.realestatemanager.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.openclassrooms.realestatemanager.R

class FilterDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.filter_dialog_fragment, null)

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle("Filter")
            .setView(view)
            .setPositiveButton("Apply filter") { dialog, which ->

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }
}