package com.example.tierdex.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.telecom.TelecomManager.EXTRA_LOCATION
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.AddDiscoveryViewModelFactory
import com.example.tierdex.R
import com.example.tierdex.TierDexApplication
import com.example.tierdex.data.entities.Coordinates
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.concurrent.TimeUnit


class AddDiscoveryFragment : Fragment() {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    lateinit var binding: AddDiscoveryFragmentBinding
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
        binding = AddDiscoveryFragmentBinding.inflate(inflater)
        if(!hasLocationPermission() && askedForLocationPermission){
            binding.addLatlon.visibility = View.VISIBLE
            binding.textInputLatlon.visibility = View.VISIBLE
        }
        binding.textInputLatlon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textlatlan.setText("lat, lon: $s")
            }
        })
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
        binding.btnLocation.setOnClickListener{
                if (hasLocationPermission()) {
                    binding.addLatlon.visibility = View.INVISIBLE
                    binding.textInputLatlon.visibility = View.INVISIBLE
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        if (location == null ){
                            locationRequest = LocationRequest()
                            locationRequest!!.interval = 50000
                            locationRequest!!.fastestInterval = 50000
                            locationRequest!!.smallestDisplacement = 170f // 170 m = 0.1 mile
                            locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
                            locationCallback = object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    locationResult?: return
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
                        }else {
                            val geocoder = Geocoder(requireContext())
                            val currentLocation = geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )
                            lat = location.latitude.toString()
                            lon = location.longitude.toString()
                            val countryCode = currentLocation[0].countryCode
                            val city = currentLocation[0].locality
                        }
                        val valuelatlan = "lat, lon: $lat, $lon"
                        latlan.text = valuelatlan
                    }
                } else if (askedForLocationPermission){
                    binding.addLatlon.visibility = View.VISIBLE
                    binding.textInputLatlon.visibility = View.VISIBLE
                }
                else {
                    requestLocationPermission()
                }
        }

        uri = requireArguments().getString("photo")
        binding.btnCamera.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)
        }

        binding.saveAction.setOnClickListener {
            addNewDisco()
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




}


