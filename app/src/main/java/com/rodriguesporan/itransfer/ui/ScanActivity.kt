package com.rodriguesporan.itransfer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.AppViewModel
import com.rodriguesporan.itransfer.data.User
import com.rodriguesporan.itransfer.databinding.ActivityScanBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Helper type alias used for analysis use case callbacks */
typealias BarcodeListener = (result: Task<MutableList<Barcode>>) -> Unit

class ScanActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private var currentWorkflowState: AppViewModel.WorkflowState =
        AppViewModel.WorkflowState.NOT_STARTED

    private val viewModel: AppViewModel by viewModels()
    private val db = Firebase.firestore

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var binding: ActivityScanBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        binding.apply {
            lifecycleOwner = this@ScanActivity
            scanActivity = this@ScanActivity
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = AppViewModel.WorkflowState.NOT_STARTED
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
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            db.collection("users")
                .whereEqualTo("googleUid", currentUser.uid)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val user: User = documents.elementAt(0).toObject(User::class.java)
                        viewModel.setUser(user)
                        try {
                            val bitmap = BarcodeEncoder()
                                .encodeBitmap(user.uid, BarcodeFormat.QR_CODE, 800, 800)

                            val imageView = findViewById<ImageView>(R.id.qr_code)
                            imageView.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            Log.e(TAG, "QR code generation failed: ", e)
                            Snackbar.make(
                                binding.coordinatorRootLayout,
                                "QR code generation failed",
                                Snackbar.LENGTH_LONG
                            ).setBackgroundTint(resources.getColor(R.color.red_900))
                                .setTextColor(resources.getColor(R.color.white)).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting user: ", exception)
                    Snackbar.make(
                        binding.coordinatorRootLayout,
                        "Error getting user",
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(resources.getColor(R.color.red_900))
                        .setTextColor(resources.getColor(R.color.white)).show()
                }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        /**
         * This is used to bind the lifecycle of cameras to the lifecycle owner
         * This eliminates the task of opening and closing the camera since CameraX is lifecycle-aware
         * */
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            },
            ContextCompat.getMainExecutor(this)
        ) // This returns an Executor that runs on the main thread
        viewModel.setWorkflowState(AppViewModel.WorkflowState.DETECTING)
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        var preview: Preview = Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(binding.previewView.surfaceProvider) }

        imageCapture = ImageCapture.Builder().build()
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { task ->
                    task
                        .addOnSuccessListener { list: MutableList<Barcode> ->
                            if (list?.size > 0 && viewModel.workflowState.value == AppViewModel.WorkflowState.DETECTING) {
                                viewModel.setWorkflowState(AppViewModel.WorkflowState.DETECTED)
                                cameraProvider.unbindAll()
                                // TODO: open a loading UI component and then check if rawValue is a valid UID
                                val receiverUid: String = list[0].rawValue
                                // TODO: call firebase to check if the user exists and then put WorkflowState to SEARCHING
                                viewModel.setWorkflowState(AppViewModel.WorkflowState.SEARCHING)
                                // TODO: after user's check put WorkflowState to CONFIRMED
                                viewModel.setWorkflowState(AppViewModel.WorkflowState.CONFIRMED)

                                val intent = Intent(this, SendPaymentActivity::class.java)
                                    .putExtra("RECEIVER_UID", receiverUid)
                                    .putExtra("SENDER_UID", viewModel.user.value?.uid)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Scanner process failed: ", exception)
                            Snackbar.make(
                                binding.coordinatorRootLayout,
                                "Scanner process failed",
                                Snackbar.LENGTH_LONG
                            ).setBackgroundTint(resources.getColor(R.color.red_900))
                                .setTextColor(resources.getColor(R.color.white)).show()
                        }
                })
            }

        var cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
            Snackbar.make(
                binding.coordinatorRootLayout,
                "Use case binding failed",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(resources.getColor(R.color.red_900))
                .setTextColor(resources.getColor(R.color.white)).show()
        }
    }

    private class BarcodeAnalyzer(private val listener: BarcodeListener) : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val inputImage =
                InputImage.fromMediaImage(imageProxy.image, imageProxy.imageInfo.rotationDegrees)
            val options =
                BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            val scanner = BarcodeScanning.getClient(options)
            val result: Task<MutableList<Barcode>> = scanner.process(inputImage)
                .addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }

            listener(result)
        }
    }

    companion object {
        private const val TAG = "ITransfer"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
}