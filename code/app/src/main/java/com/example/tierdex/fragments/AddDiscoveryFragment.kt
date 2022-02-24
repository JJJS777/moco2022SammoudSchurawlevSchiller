package com.example.tierdex.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.AddDiscoveryViewModelFactory
import com.example.tierdex.R
import com.example.tierdex.TierDexApplication
import com.example.tierdex.data.entities.Coordinates
import com.example.tierdex.databinding.FragmentAddDiscoveryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class AddDiscoveryFragment : Fragment() {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    lateinit var binding: FragmentAddDiscoveryBinding
    private var uri: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var currentLocation: Location? = null

    private var lat ="37.4220"
    private var lon ="-122.0840"
    private var askedForLocationPermission = false


    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: AddDiscoveryViewModel by activityViewModels {
        AddDiscoveryViewModelFactory(
            (activity?.application as TierDexApplication).database.discoveredDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDiscoveryBinding.inflate(inflater)
        if(!hasLocationPermission() && askedForLocationPermission){
            binding.manualLocationInput.visibility = View.VISIBLE
        }
        return binding.root
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewDisco() {
        viewModel.addNewDiscovery(
            binding.textInputAnimalName.text.toString(),
            coordinates = Coordinates( lat, lon )
        )
    }

    //where you can then bind properties to specific views
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val latlan = binding.textlatlan
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.btnLocation.setOnClickListener {
            while (true) {
                if (hasLocationPermission()) {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        if (location == null) {
                            locationRequest = LocationRequest()
                            locationRequest.interval = 50000
                            locationRequest.fastestInterval = 50000
                            locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
                            locationRequest.priority =
                                LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
                            locationCallback = object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    locationResult ?: return
                                    if (locationResult.locations.isNotEmpty()) {
                                        // get latest location
                                        val location = locationResult.lastLocation
                                        lat = location.latitude.toString()
                                        lon = location.longitude.toString()
                                        val valuelatlan = "lat, lon: $lat, $lon"
                                        latlan.text = valuelatlan
                                    }
                                }
                            }
                        } else {
                            lat = location.latitude.toString()
                            lon = location.longitude.toString()
                        }
                        val geocoder = Geocoder(requireContext())
                        val currentLocation = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        val countryName = currentLocation[0].countryName
                        val city = currentLocation[0].locality
                        val plz = currentLocation[0].postalCode
                        binding.textCity.setText(city)
                        binding.textCountry.setText(countryName)
                        binding.textPostcode.setText(plz)
                        val valuelatlan = "lat, lon: $lat, $lon"
                        latlan.text = valuelatlan
                    }
                    break;
                } else {
                    if (askedForLocationPermission)
                        break;
                    else
                    requestLocationPermission()
                }
            }
        }
        uri = requireArguments().getString("photo")

        if(uri != null){
            setPhoto(uri!!.toUri())
        }

        binding.btnCamera.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)
        }

        binding.searchAnimalButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate( R.id.action_addDiscoveryFragment_to_animalListFragment )
        }

        binding.saveAction.setOnClickListener {
            addNewDisco()
            if (uri != null){
                saveToFirebase(uri!!)
            }
        }

    }

    //returns true if permission granted
    private fun hasLocationPermission()=
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

    //request permission
    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This App cannot work without Location permission",
            PERMISSION_LOCATION_REQUEST_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        askedForLocationPermission = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setPhoto(uri: Uri){
        binding.photoView.let { photo ->
            photo.post {
                Glide.with(photo)
                    .load(uri)
                    .into(photo)
            }
        }
    }

    private fun saveToFirebase(uri : String){
        val storageRef = Firebase.storage.reference

        // change metadata how u like
        val metadata = storageMetadata {
            contentType = "image/jpg"
            setCustomMetadata("name","hier kommt name")
            setCustomMetadata("ort", "hier kommt ort")
        }
        // name muss auch demenstprechen angepasst werden -> ola muss raus
        val images = storageRef.child("images/ola")

        val uploadTask = images.putFile(uri.toUri(),metadata)


        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            Toast.makeText(requireContext(),"Upload successfull",Toast.LENGTH_LONG).show()
        }

    }

}


