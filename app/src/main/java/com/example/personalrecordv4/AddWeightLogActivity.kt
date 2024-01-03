package com.example.personalrecordv4

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.personalrecordv4.databinding.ActivityAddWeightLogBinding
import com.example.personalrecordv4.viewmodel.WeightLogViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.math.abs

class AddWeightLogActivity : AppCompatActivity() {
    private val weightLogViewModel : WeightLogViewModel by viewModels()
    private lateinit var unwrappedBitmap: Bitmap
    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private val addweight : ActivityAddWeightLogBinding by lazy{
        ActivityAddWeightLogBinding.inflate(layoutInflater)
    }
    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 26){
        arrayListOf(
            android.Manifest.permission.CAMERA
        )
    } else {
        arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(addweight.root)
        addweight.ivDone.visibility = View.GONE
        if (checkMultiplePermission()){
            startCamera()
        }
        addweight.ivBackPhoto.setOnClickListener {
            finish()
        }

        addweight.ivDone.setOnClickListener {

            val x = addweight.etWeightInput.text.toString()

            if (!weightLogViewModel.isWeightEmpty(x)){
                addweight.etWeightInput.error = "Please Input Your Weight"
                return@setOnClickListener
            }
            var weight = x.toDouble()
            // Upload the bitmap to Firebase Cloud Storage
            if (!weightLogViewModel.isWeightValid(weight)){
                Log.i("AddWeightLogActivity","Weight InValid")
                addweight.etWeightInput.error = "Invalid Weight Input"
                return@setOnClickListener
            }
            Log.i("AddWeightLogActivity","Weight Valid")
            weightLogViewModel.addWeightLog(unwrappedBitmap, weight)
            Toast.makeText(this@AddWeightLogActivity, "Weight Log Added", Toast.LENGTH_SHORT).show()
            finish()

        }

        addweight.ivSwitch.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT)
            {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            bindCameraUserCases()
        }
        addweight.ivCamera.setOnClickListener {
            takePhoto()
        }
        addweight.ivFlash.setOnClickListener {
            setFlashIcon(camera)
        }
    }
    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    startCamera()
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        appSettingOpen(this)
                    } else {
                        warningPermissionDialog(this) { _: DialogInterface, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE ->
                                    checkMultiplePermission()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        },ContextCompat.getMainExecutor(this))
    }
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = maxOf(width, height).toDouble() / minOf(width,height)
        return if (abs(previewRatio - 4.0/3.0) <= abs(previewRatio - 16.0 / 9.0)){
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }
    private fun bindCameraUserCases(){
        val screenAspectRatio = aspectRatio(
            addweight.cameraPreview.width,
            addweight.cameraPreview.height
        )
        val rotation = addweight.cameraPreview.display.rotation
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    screenAspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            ).build()
        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(addweight.cameraPreview.surfaceProvider)
            }
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e:Exception){
            Log.i("AddWeightLogActivity","Exception : $e")
            e.printStackTrace()
        }
    }
    private fun setFlashIcon(camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()){
            if (camera.cameraInfo.torchState.value == 0){
                camera.cameraControl.enableTorch(true)
            } else {
                camera.cameraControl.enableTorch(false)
            }
        } else {
            Toast.makeText(
                this, "Flash is not available", Toast.LENGTH_SHORT
            ).show()
            addweight.ivFlash.isEnabled = false
        }
    }

private fun takePhoto() {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(@NonNull image: ImageProxy) {
                // Convert the image to a bitmap
//                val bitmap = imageToBitmap(image)
                val imageProxyBuffer = image.planes[0].buffer
                val imageBytes = ByteArray(imageProxyBuffer.remaining())
                imageProxyBuffer.get(imageBytes)
                unwrappedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                // Show the captured image on the ImageView
                addweight.ivreview.setImageBitmap(unwrappedBitmap)

                // Hide the preview view (optional)
                addweight.cameraPreview.visibility = View.GONE

                // Show the save button
                addweight.ivDone.visibility = View.VISIBLE

                // Close the image proxy
                image.close()

            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@AddWeightLogActivity, "ERROR: " + exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    )

}
//    private fun imageToBitmap(image: ImageProxy): Bitmap {
//        val buffer = image.planes[0].buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer.get(bytes)
//        return BitmapFactory.decodeByteArray(bytes, 0,bytes.size)
//    }


}
