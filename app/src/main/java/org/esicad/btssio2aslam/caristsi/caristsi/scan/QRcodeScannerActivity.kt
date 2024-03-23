package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.AddPackageDialog
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.PackageCard
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@AndroidEntryPoint
class QRcodeScannerActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CAMERA = 1

    val scannedPackage: MutableState<Package?> = mutableStateOf(null)
    val showAddPackageModal: MutableState<Boolean> = mutableStateOf(false)

    private val addPackageViewModel: AddPackageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        addPackageViewModel.addPackageResult.observe(this@QRcodeScannerActivity, Observer {
            val addingResult = it ?: return@Observer

            if (addingResult.error != null) {
                Toast.makeText(
                    applicationContext,
                    "Impossible d'ajouter le colis",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (addingResult.success != null) {
                Toast.makeText(
                    applicationContext,
                    "Colis ajouté",
                    Toast.LENGTH_SHORT
                ).show()
            }

            showAddPackageModal.value = false
        })


        setContent {
            CaristSITheme {

                QRcodeScanner()
                if (showAddPackageModal.value) {
                    addPackageViewModel.newPackage.value?.let {
                        AddPackageDialog(
                            // `package` = scannedPackage.value,
                            `package` = it,
                            onDismissRequest = { showAddPackageModal.value = false },
                            onConfirmRequest = { newPackage ->
                                addPackageViewModel.addPackage(newPackage)
                            }
                        )
                    }
                }
            }
        }
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA
            )
        } else {
            //initQRCodeScanner()
        }
    }

    private fun initQRCodeScanner() {
        // Initialize QR code scanner here
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setOrientationLocked(false)
        integrator.setPrompt(getString(R.string.package_scan_label))
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner()
            } else {
                Toast.makeText(
                    this, "Camera permission is required", Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val resultData = Intent()
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
                setResult(RESULT_CANCELED, null)
            } else {
                try {
                    resultData.setData(Uri.parse(result.contents))
                    Log.i("I", "QR code scanned: ${result.contents}")
                    addPackageViewModel.setNewPackage(Gson().fromJson(result.contents, Package::class.java))
                    Toast.makeText(this, "Colis scanné !", Toast.LENGTH_LONG).show()
                    // decode json data from qr code
                    showAddPackageModal.value = true
                    Log.i("I", "Data decoded : ${scannedPackage.value}")
                } catch (e: Exception) {
                    Log.e("I", "Impossible de décoder les données du colis", e)
                    Toast.makeText(
                        this,
                        "Impossible de décoder les données du colis",
                        Toast.LENGTH_LONG
                    ).show()
                }


                //setResult(RESULT_OK, resultData)
            }

            //finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview
    fun QRcodeScanner() {

        val packages = listOf(
            Package(1, "123456", "1234567890123", "Cable usb C"),
            Package(2, "654321", "3210987654321", "2022-12-31"),
            Package(3, "987654", "9876543210123", "2022-12-31")
        )

        CaristSITheme {
            Scaffold(modifier = Modifier.safeDrawingPadding(), topBar = {
                CenterAlignedTopAppBar(title = { Text("Scanner de colis") }, navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                })
            }) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                        Text(text = "Scannez un code QR pour commencer")
                    // PackageCard(packages.first(), {})
                    Button(onClick = { initQRCodeScanner() }) {
                        Text(text = "Scanner un code QR")
                    }
                }
            }
        }
    }




}

@Preview
@Composable
fun PreviewAddPackageDialog() {
    var scannedPackage by remember {
        mutableStateOf<Package>(
            Package(
                1,
                "123456",
                "1234567890123",
                ""
            )
        )
    }
    CaristSITheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AddPackageDialog(scannedPackage, {})
        }
    }
}

