package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.AddPackageDialog
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@AndroidEntryPoint
class QRcodeScannerActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CAMERA = 1

    val scannedPackage: MutableState<Package?> = mutableStateOf(null)
    val showAddPackageModal: MutableState<Boolean> = mutableStateOf(false)

    private val addPackageViewModel: AddPackageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()

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

                QRcodeScanner(
                    onPackageScanned = { scannedPackage ->
                        addPackageViewModel.setNewPackage(scannedPackage)
                        showAddPackageModal.value = true
                    }
                )
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

    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    @Composable
    fun QRcodeScanner(onPackageScanned: (`package`: Package) -> Unit) {

        val packages = listOf(
            Package(1, "123456", "1234567890123", "Cable usb C"),
            Package(2, "654321", "3210987654321", "2022-12-31"),
            Package(3, "987654", "9876543210123", "2022-12-31")
        )


        val scanLauncher =
            rememberLauncherForActivityResult(contract = ScanContract(), onResult = { result ->
                if (result.contents != null) {
                    try {
                        val newPackage: Package =
                            Gson().fromJson(result.contents, Package::class.java)
                        Toast.makeText(this, "Colis scanné !", Toast.LENGTH_LONG).show()
                        Log.i("QRCodeScannerActivity", "Data decoded : ${newPackage}")
                        onPackageScanned(newPackage)
                    } catch (e: JsonSyntaxException) {
                        Log.e(
                            "QRCodeScannerActivity", "Impossible de décoder les données du colis", e
                        )
                        Toast.makeText(this, "Colis invalide !", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Log.e("QRCodeScannerActivity", "Erreur interne", e)
                        Toast.makeText(this, "Erreur interne", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.i("QRCodeScannerActivity", "scan cancelled")
                }
            })


        val scanOptions: ScanOptions = setupScanOptions()

        handleCameraPermission { isGranted ->
            if (isGranted) {
                scanLauncher.launch(scanOptions)
            } else {
                finish()
                // TODO handle permissions gracefully
            }
        }

        ScannerScreen(backAction = { finish() }, scanAction = { scanLauncher.launch(scanOptions) })
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun handleCameraPermission(onPermissionResult: (Boolean) -> Unit) {
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
                onPermissionResult(isGranted)
        }

        LaunchedEffect(cameraPermissionState) {
            if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
                Toast.makeText(
                    this@QRcodeScannerActivity, "Camera permission is required", Toast.LENGTH_LONG
                ).show()
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }

                startActivity(intent)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    @Composable
    private fun setupScanOptions(): ScanOptions {
        return ScanOptions()
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            .setPrompt(stringResource(R.string.package_scan_label))
            .setBeepEnabled(false)
            .setOrientationLocked(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ScannerScreen (label: String = "Scanner de colis", backAction: () -> Unit = {}, scanAction: () -> Unit = {}) {
    CaristSITheme {
        Scaffold(modifier = Modifier.safeDrawingPadding(), topBar = {
            CenterAlignedTopAppBar(title = { Text(label) }, navigationIcon = {
                IconButton(onClick = { backAction() }) {
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
                Button(onClick = { scanAction() }) {
                    Text(text = "Scanner un code QR")
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

