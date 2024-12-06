package com.ht.qrscanner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.ht.qrscanner.databinding.ActivityQractivityBinding

class QRActivity : AppCompatActivity() {
    companion object
    {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, QRActivity::class.java)
        }
    }
    lateinit var binding:ActivityQractivityBinding
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQractivityBinding.inflate(layoutInflater)
        setContentView(binding.root )
        codeScanner = CodeScanner(this, binding.qrCodeScanner)

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                setupCodeScanner()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                showPermissionRationale()
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupCodeScanner()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this).setTitle("Permission Required")
            .setMessage("Camera access is needed to scan QR codes.")
            .setPositiveButton("Allow") { _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }.setNegativeButton("Deny", null).show()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupCodeScanner() {
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        codeScanner.decodeCallback = DecodeCallback {
         // everything should be in UI thread
            runOnUiThread {
                startActivity(TextCopyActivity.getCallingIntent(this@QRActivity, it.text))
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
    }
}