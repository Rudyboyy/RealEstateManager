package com.openclassrooms.realestatemanager.ui.add

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.CheckboxAdapter
import com.openclassrooms.realestatemanager.adapter.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.PropertyStatus
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.utils.CheckBoxOptionProvider.getOptions
import com.openclassrooms.realestatemanager.utils.FragmentUtils.handleBackPressed
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private val actionFragment: Int = R.id.action_global_to_PropertyListFragment
    private val propertyPhotos = mutableListOf<Photo>()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var checkboxAdapter: CheckboxAdapter
    private var isNew = true
    private var mId: Long = 0
    private var currentProperty: Property? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPressed(actionFragment)
        initPhotoRecyclerView(propertyPhotos)
        initCheckboxRecyclerView()
        setPropertyToEdit()
        setAddPhotoButton()
        setBackButton()
        setSaveButton()
        setSoldButton()
    }

    private fun setSoldButton() {
        if (!isNew) {
            binding.soldButton.visibility = View.VISIBLE
            binding.soldButton.setOnClickListener {
                showDatePickerDialog()
            }
        } else {
            binding.soldButton.visibility = View.GONE
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val date = selectedDate.time
                handleSelectedDate(date)
            },
            mYear,
            mMonth,
            mDay
        )

        datePickerDialog.show()
    }

    private fun handleSelectedDate(date: Date) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.confirmation_message, formattedDate))
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                val updateProperty =
                    currentProperty?.copy(saleDate = date, status = PropertyStatus.SOLD)
                if (updateProperty != null) {
                    viewModel.update(updateProperty)
                    findNavController().navigate(actionFragment)
                    showToast(true)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }

    @Suppress("DEPRECATION")
    private fun setPropertyToEdit() {
        val property: Property? = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("property", Property::class.java)
        } else {
            arguments?.getParcelable("property")
        }
        if (property != null) {
            currentProperty = property
            isNew = false
            mId = property.id
            val poi = property.pointOfInterest.split(", ")
            binding.saveButton.text = getString(R.string.update)
            propertyPhotos.addAll(property.photos)
            initPhotoRecyclerView(propertyPhotos)
            binding.textAgent.setText(property.agent)
            binding.textType.setText(property.type)
            binding.textPrice.setText(property.price.toString())
            binding.textSurface.setText(property.surface.toString())
            binding.numRoom.setText(property.numberOfRooms.toString())
            binding.numBedroom.setText(property.numberOfBedrooms.toString())
            binding.numBathroom.setText(property.numberOfBathrooms.toString())
            binding.textDescription.setText(property.description)
            binding.textAddress.setText(property.address)
            checkboxAdapter.checkedItems.addAll(poi)
        }
    }

    private fun setAddPhotoButton() {
        binding.addButton.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForPhotoResult.launch(intent)
                    binding.progressBar.visibility = View.VISIBLE
                }
        }
    }

    private fun initCheckboxRecyclerView() {
        val checkboxRecyclerView = binding.checkboxRecyclerview
        val screenWidth = resources.displayMetrics.widthPixels
        val desiredColumnCount = if (screenWidth < 1100) 2 else 3
        val gridLayoutManager = GridLayoutManager(requireContext(), desiredColumnCount)
        checkboxAdapter = CheckboxAdapter(getOptions(requireContext()))
        checkboxRecyclerView.layoutManager = gridLayoutManager
        checkboxRecyclerView.adapter = checkboxAdapter
    }

    private fun initPhotoRecyclerView(photos: List<Photo>) {
        photoAdapter = PhotoAdapter(
            onItemClicked = { position ->
                val photoUris = photos.map { it.uri }
                val photoDescriptions = photos.map { it.description }
                val action =
                    AddPropertyFragmentDirections.actionGlobalToImageSlideDialogFragment(
                        photoUris.toTypedArray(),
                        photoDescriptions.toTypedArray(),
                        position
                    )
                findNavController().navigate(action)
            },
            onItemDeleteClicked = {
                propertyPhotos.removeAt(it)
                photoAdapter.notifyItemRemoved(it)
            }
        )
        binding.addPhotoRecyclerview.adapter = photoAdapter
        photoAdapter.setInAddFragment(true)
        photoAdapter.submitList(photos)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val startForPhotoResult =
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
            photoAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        }

    private fun dialogPhotoDescription(fileUri: Uri) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        dialogBuilder.setTitle(getString(R.string.photo_description))
        dialogBuilder.setView(input)

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
        val fields = listOf(
            binding.textAgent to binding.textFieldAgent,
            binding.textType to binding.textFieldType,
            binding.textPrice to binding.textFieldPrice,
            binding.textSurface to binding.textFieldSurface,
            binding.numRoom to binding.textFieldRoom,
            binding.numBathroom to binding.textFieldBathroom,
            binding.numBedroom to binding.textFieldBedroom,
            binding.textDescription to binding.textFieldDescription,
            binding.textAddress to binding.textFieldAddress
        )

        binding.saveButton.setOnClickListener {
            var isValid = true

            for ((textInput, textField) in fields) {
                val input = textInput.text.toString()
                if (input.isEmpty()) {
                    textField.error = getString(R.string.error_empty_field, textInput.hint)
                    isValid = false
                } else {
                    textField.error = null
                }
            }

            if (isValid) {
                saveProperty()
                findNavController().navigate(actionFragment)
                sendVisualNotification()
            }
            showToast(isValid)
        }
    }

    private fun saveProperty() {
        setDoubleFormat(binding.textSurface)
        setDoubleFormat(binding.textPrice)
        val agent = "${binding.textAgent.text}"
        val type = "${binding.textType.text}"
        val price = binding.textPrice.text.toString().toDouble()
        val description = "${binding.textDescription.text}"
        val address = "${binding.textAddress.text}"
        val numRoom = binding.numRoom.text.toString().toInt()
        val numBedroom = binding.numBedroom.text.toString().toInt()
        val numBathroom = binding.numBathroom.text.toString().toInt()
        val surface = binding.textSurface.text.toString().toDouble()
        val latFlow = viewModel.latitude.asFlow()
        val lgtFlow = viewModel.longitude.asFlow()
        val poi = getPoi()
        viewModel.updateCoordinatesFromAddress(address)
        lifecycleScope.launch {
            combine(latFlow, lgtFlow) { lat, lgt ->
                val property = Property(
                    id = mId,
                    agent = agent,
                    type = type,
                    price = price,
                    description = description,
                    address = address,//todo need to change the way to get the address with postal code country city etc to get the right position with geocoding
                    status = PropertyStatus.AVAILABLE,
                    numberOfRooms = numRoom,
                    numberOfBedrooms = numBedroom,
                    numberOfBathrooms = numBathroom,
                    surface = surface,
                    pointOfInterest = poi,
                    entryDate = Date(System.currentTimeMillis()),
                    latitude = lat,
                    longitude = lgt,
                    photos = propertyPhotos
                )
                if (isNew) {
                    viewModel.addProperty(property)
                } else {
                    viewModel.update(property)
                }
            }.collect()
        }
    }

    private fun showToast(success: Boolean) {
        var message =
            if (isNew) getString(R.string.property_created) else getString(R.string.property_updated)
        if (!success) {
            message = getString(R.string.missing_information)
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }


    private fun setDoubleFormat(text: TextInputEditText) {
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                if (input.isNotEmpty() && !input.contains('.')) {
                    text.removeTextChangedListener(this)
                    text.setText(getString(R.string.price_format_with_decimal, input))
                    text.addTextChangedListener(this)
                    val lastPosition = input.length + 2
                    text.setSelection(lastPosition)
                }
            }
        })
    }

    private fun getPoi(): String {
        val selectedPoiList = mutableListOf<String>()
        selectedPoiList.addAll(checkboxAdapter.checkedItems)

        return if (selectedPoiList.isEmpty()) {
            ""
        } else {
            selectedPoiList.joinToString(", ")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendVisualNotification() {
        if (isNew) {
            val notificationId = 7
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent =
                PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId: String = requireContext().getString(R.string.channel_id)
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(requireContext(), channelId)
                    .setSmallIcon(R.drawable.real_estate)
                    .setContentTitle(requireContext().getString(R.string.app_name))
                    .setContentText(getString(R.string.new_property_added))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            val notificationManager =
                requireContext().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

            // Support Version >= Android 8
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName: CharSequence = requireContext().getString(R.string.channel_name)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, channelName, importance)
                notificationManager.createNotificationChannel(mChannel)
            }

            // Show notification
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}



























