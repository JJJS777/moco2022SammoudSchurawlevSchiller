package com.example.tierdex.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.AddDiscoveryViewModelFactory
import com.example.tierdex.R
import com.example.tierdex.TierDexApplication
import com.example.tierdex.data.entities.Coordinates
import com.example.tierdex.databinding.FragmentAddDiscoveryBinding
import com.google.android.gms.location.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.vmadalin.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream


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
    ): View {
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
                                    locationResult?: return
                                    if (locationResult.locations.isNotEmpty()) {
                                        // get latest location
                                        val location = locationResult.lastLocation
                                            lat = location.latitude.toString()
                                            lon = location.longitude.toString()
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
                                }
                            }
                        } else {
                            lat = location.latitude.toString()
                            lon = location.longitude.toString()
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
        Log.d("TEST",uri.toString())
        if(uri.toString() != "None" ){
            setPhoto(uri!!.toUri())
        }

        binding.btnCamera.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)
        }

        binding.saveAction.setOnClickListener {
            if(binding.textInputAnimalName.text.toString() != ""){
                addNewDisco()
                if (uri.toString() != "None"  && checkInternetState()){
                    saveToFirebase(uri!!)
                }else if (checkInternetState()){
                    val drawable = binding.photoView.drawable.toBitmap()
                    val newUri = getImageUri(drawable)
                    saveToFirebase(newUri.toString())
                }
            }else{
                Toast.makeText(context,"Animal Name is mandotary",Toast.LENGTH_SHORT).show()
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


    private fun checkInternetState() : Boolean{
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            return false
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context?.contentResolver , inImage, "TierDex", null)
        return Uri.parse(path)
    }

    private fun saveToFirebase(uri : String){
        val storageRef = Firebase.storage.reference

        val tsLong = System.currentTimeMillis() / 1000

        // change metadata how u like
        val metadata = storageMetadata {
            contentType = "image/jpg"
            setCustomMetadata("timestamp",tsLong.toString())
            setCustomMetadata("name",binding.textInputAnimalName.text.toString())
            setCustomMetadata("ort", binding.textCity.text.toString())
            setCustomMetadata("postcode",binding.textPostcode.text.toString())
            setCustomMetadata("description",binding.textInputDescription.text.toString())
        }
        val images = storageRef.child("images/"+binding.textInputAnimalName.text.toString())

        val uploadTask = images.putFile(uri.toUri(),metadata)


        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            Toast.makeText(requireContext(),"Upload successfull",Toast.LENGTH_LONG).show()
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_addDiscoveryFragment_to_homeFragment) }
        }

    }

}


