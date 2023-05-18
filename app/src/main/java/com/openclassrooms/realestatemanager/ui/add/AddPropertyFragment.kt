package com.openclassrooms.realestatemanager.ui.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import com.openclassrooms.realestatemanager.ui.property.PropertyListFragmentDirections
import com.openclassrooms.realestatemanager.utils.FragmentUtils.handleBackPressed
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private val actionFragment: Int = R.id.action_global_to_PropertyListFragment
    private val propertyPhotos = mutableListOf<Photo>()
    private lateinit var adapter: PhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPressed(actionFragment)
        binding.addButton.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                    binding.progressBar.visibility = View.VISIBLE
                }
        }
        initRecyclerView(propertyPhotos)

        setBackButton()
        setSaveButton()
    }

    private fun initRecyclerView(photos: List<Photo>) {
        adapter = PhotoAdapter { position ->
            val photoUris = photos.map { it.uri }
            val photoDescriptions = photos.map { it.description }
            val action =
                PropertyListFragmentDirections.actionPropertyListFragmentToImageSlideDialogFragment(
                    photoUris.toTypedArray(),
                    photoDescriptions.toTypedArray(),
                    position
                )
            findNavController().navigate(action)
        }
        binding.addPhotoRecyclerview.adapter = adapter
        adapter.submitList(photos)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    lifecycleScope.launch {
                        val fileUri = data?.data!!
                        dialogPhotoDescription(fileUri)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.task_cancelled),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            adapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        }

    private fun dialogPhotoDescription(fileUri: Uri) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.photo_description))

        val input = EditText(requireContext())
        dialogBuilder.setView(input)
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        dialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            val description = input.text.toString()

            val photo = Photo(uri = fileUri.toString(), description = description)

            propertyPhotos.add(photo)

            inputMethodManager.hideSoftInputFromWindow(input.windowToken, 0)

            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            inputMethodManager.hideSoftInputFromWindow(input.windowToken, 0)

            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(actionFragment)
        }
    }

    private fun setSaveButton() {
        binding.saveButton.setOnClickListener {
            createProperty()
            findNavController().navigate(actionFragment)
        }
    }

    private fun createProperty() {
        val address = "${binding.textAddress.text}"
        val latFlow = viewModel.latitude.asFlow()
        val lgtFlow = viewModel.longitude.asFlow()
        val poi = getPoi()
//todo exemple MyReu pour geré quand le text n'est pas rempli est qu'il faut le remplir(+geré les double)
        viewModel.updateCoordinatesFromAddress(address)
        lifecycleScope.launch {
            combine(latFlow, lgtFlow) { lat, lgt ->
                val property = Property(
                    id = 0,
                    agent = "${binding.textAgent.text}",
                    type = "${binding.textType.text}",
                    price = binding.textPrice.text.toString().toDouble(),
                    description = "${binding.textDescription.text}",
                    address = address,
                    status = PropertyStatus.AVAILABLE,
                    numberOfRooms = binding.numRoom.text.toString().toInt(),
                    numberOfBedrooms = binding.numBedroom.text.toString().toInt(),
                    numberOfBathrooms = binding.numBathroom.text.toString().toInt(),
                    surface = binding.textSurface.text.toString().toDouble(),
                    pointOfInterest = poi,
                    entryDate = Date(System.currentTimeMillis()),
                    latitude = lat,
                    longitude = lgt,
                    photos = propertyPhotos
                )
                viewModel.addProperty(property)
            }.collect()
        }
    }

    private fun getPoi(): String {
        val grid = binding.checkbox
        val selectedPoiList = mutableListOf<String>()
            if (grid.checkboxNearbySchool.isChecked) {
                selectedPoiList.add(getString(R.string.school))
            }
            if (grid.checkboxNearbyPark.isChecked) {
                selectedPoiList.add(getString(R.string.park))
            }
            if (grid.checkboxNearbyHospital.isChecked) {
                selectedPoiList.add(getString(R.string.hospital))
            }
            if (grid.checkboxNearbySupermarket.isChecked) {
                selectedPoiList.add(getString(R.string.supermarket))
            }
            if (grid.checkboxNearbyPharmacy.isChecked) {
                selectedPoiList.add(getString(R.string.pharmacy))
            }
            if (grid.checkboxNearbyGasStation.isChecked) {
                selectedPoiList.add(getString(R.string.gas_station))
            }
            if (grid.checkboxNearbyEatery.isChecked) {
                selectedPoiList.add(getString(R.string.eatery))
            }
            if (grid.checkboxNearbyShop.isChecked) {
                selectedPoiList.add(getString(R.string.shopping_center_mall))
            }
            if (grid.checkboxNearbyPublicTransportation.isChecked) {
                selectedPoiList.add(getString(R.string.public_transportation))
            }
        return if (selectedPoiList.isEmpty()) {
            ""
        } else {
            selectedPoiList.joinToString(", ")
        }
    }
}



























