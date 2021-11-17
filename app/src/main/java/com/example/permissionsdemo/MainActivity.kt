package com.example.permissionsdemo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    //Todo 1: This time we create the Activity result launcher of type Array<String>
    private  val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
                isGranted ->
                if(isGranted){
                    Toast.makeText(
                        this,
                        "Permission granted for camera",
                        Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(
                        this,
                        "Permission denied for camera",
                        Toast.LENGTH_LONG).show()
                }
        }

    private  val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            /**
            Here it returns a Map of permission name as key with boolean as value
            Todo 2: We loop through the map to get the value we need which is the boolean
            value
             */
            permissions.entries.forEach{
                val permissionName = it.key
                //Todo 3: if it is granted then we show its granted
                val isGranted = it.value
                if(isGranted) {
                    //check the permission name and perform the specific operation
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission granted for fine location",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission granted for coarse location",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            this,
                            "Permission granted for camera",
                            Toast.LENGTH_LONG).show()
                    }

                }else{ //check the permission name and perform the specific operation
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission denied for fine location",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission denied for coarse location",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            this,
                            "Permission denied for camera",
                            Toast.LENGTH_LONG).show()
                    }
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnCameraPermission: Button = findViewById(R.id.btn_camera_permission)
        btnCameraPermission.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationaleDialog("Permission Demo requires camera access",
                "Camera cannot be used because Camera access is denied")
            }else{
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                cameraAndLocationResultLauncher.launch(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }

    }

    /**
     * Show rationale dialog for displaying why the app needs permission
     * Only shown if the user has denied the permission request previously
     */
    private  fun showRationaleDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this,)
        builder.setTitle(title).setMessage(message).setPositiveButton("cancel"){
            dialog,_->dialog.dismiss()
        }
        builder.create().show()
    }
}