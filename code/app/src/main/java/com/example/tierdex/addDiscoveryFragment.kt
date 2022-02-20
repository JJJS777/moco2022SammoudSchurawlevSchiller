package com.example.tierdex

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.add_discovery_fragment.view.*
import java.util.jar.Manifest
import android.R
import android.widget.EditText

class addDiscoveryFragment : Fragment() {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private lateinit var viewModel: AddDiscoveryViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.tierdex.R.layout.add_discovery_fragment, container, false)
        view.btnCamera.setOnClickListener{Navigation.findNavController(view).navigate(com.example.tierdex.R.id.action_addDiscoveryFragment_to_cameraLayout)}
        return view
    }

@SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var binding : AddDiscoveryFragmentBinding
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if(hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.d("addDiscovery", location.latitude.toString())
                Log.d("addDiscovery", location.longitude.toString())
                val valuelatlan =  location.latitude.toString() +", "+ location.longitude.toString()
                binding.textlatlan.setText(valuelatlan)
            }
        }else{
            requestLocationPermission()
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddDiscoveryViewModel::class.java)
        // TODO: Use the ViewModel
    }
}


