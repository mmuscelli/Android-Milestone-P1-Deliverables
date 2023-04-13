package com.example.cameraapplication

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraapplication.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Executors.newSingleThreadExecutor

class MainActivity : AppCompatActivity() {
    // 0. Class for View Binding
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 1. CameraX component; ImageCapture use case
    private var imageCapture: ImageCapture? = null

    // 2. CameraX component; CameraExecutor (ExecutorService)
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // Request necessary permissions
        if (REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
            }) {
            // All the required permission have been granted. Start camera features
            startCamera()
        } else {
            // There are no or missing permission that has to be granted before start our camera service.
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION
            )
        }

        // Connect takePhoto function and button's event listener
        viewBinding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }

        // Start camera service on separated thread using ExecutorService
        cameraExecutor = newSingleThreadExecutor()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSION = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        ).apply {
            // If device is running Android version lower than 9, an additional permission is required.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (REQUIRED_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        baseContext, it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                // All the required permission are granted
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permission not granted by the user.",
                    Toast.LENGTH_LONG).show()
                finish() // Close the application because it cannot run without the permission
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview use case
            val preview = Preview.Builder()
                .build()
                .also {
                    // Get SurfaceProvider from PreviewView
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // ImageCapture use case
//            val imageCapture = ImageCapture.Builder().build()

            // Camera selector: select camera to be used
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("CameraImp", "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        Toast.makeText(baseContext, "Cheese!!!", Toast.LENGTH_LONG).show()
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())

        // ContentValue object is a special type of object that is used to
        // store value in local storage using ContentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraApp")
            }
        }

        // Store image at local storage using ContentResolver
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is invoked after image capture
        // is made
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    Toast.makeText(baseContext,
                        "Photo capture succeeded: ${outputFileResults.savedUri}",
                        Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraImp",
                        "Photo capture failed: ${exception.message}",
                        exception)
                }
            }
        )
    }

}