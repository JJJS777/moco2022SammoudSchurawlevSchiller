package com.example.tierdex.fragments

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
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


class AddDiscoveryFragment : Fragment() {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    lateinit var binding: AddDiscoveryFragmentBinding
    private var uri: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lat =""
    private var lon =""
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
            while (true) {
                if (hasLocationPermission()) {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        if (location == null || location.accuracy > 100){
                            lat =  "51.0241451"
                            lon = "7.5629562"
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
                        val valuelatlan = "lat, lan: $lat, $lon"
                        latlan.text = valuelatlan

                    }
                    break;
                } else {
                    if (!askedForLocationPermission)
                    requestLocationPermission()
                    else
                        break;
                }
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

    fun onPermissionsDenied(requestCode: Int,perms : List<String>){
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestLocationPermission()
        }
    }
    fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
    }

}


