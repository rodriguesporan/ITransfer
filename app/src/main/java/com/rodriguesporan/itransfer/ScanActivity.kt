package com.rodriguesporan.itransfer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.rodriguesporan.itransfer.databinding.ActivityScanBinding
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.rodriguesporan.itransfer.model.Transaction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Helper type alias used for analysis use case callbacks */
typealias BarcodeListener = (result: Task<MutableList<Barcode>>) -> Unit

class ScanActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var binding: ActivityScanBinding
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        binding.apply {
            lifecycleOwner = this@ScanActivity
            scanActivity = this@ScanActivity
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        reference = Firebase.database.reference

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        writeNewTransaction("userIdOne", "transactionIdOne", 100.0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun writeNewTransaction(userId: String, transactionId: String, amount: Double) {
        val transaction = Transaction(amount)
        reference.child("transactions").child(userId).child(transactionId).setValue(transaction)

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        /**
         * This is used to bind the lifecycle of cameras to the lifecycle owner
         * This eliminates the task of opening and closing the camera since CameraX is lifecycle-aware
         * */
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this)) // This returns an Executor that runs on the main thread
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        var preview: Preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder().build()
        val imageAnalyzer = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { result ->
                    result.addOnSuccessListener { list: MutableList<Barcode>? ->
                        if (list != null && list.size > 0) {
                            TONE.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 100)
                            for (code in list) {
                                Toast.makeText(this, "RawValue: ${code.rawValue}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }

        var cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer)
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    private class BarcodeAnalyzer(private val listener: BarcodeListener) : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(image: ImageProxy) {
            val inputImage = InputImage.fromMediaImage(image.image, image.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            val scanner = BarcodeScanning.getClient(options)
            val result: Task<MutableList<Barcode>> = scanner.process(inputImage)
                .addOnFailureListener { e -> Log.e(TAG, e.toString()) }
                .addOnCompleteListener {
                    image.close()
                    image.image?.close()
                }

            listener(result)
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private val TONE = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    }
}