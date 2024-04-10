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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.AddPackageDialog
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.ScannerScreen
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@AndroidEntryPoint
class AddPackageActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CAMERA = 1

    val scannedPackage: MutableState<Package?> = mutableStateOf(null)
    val showAddPackageModal: MutableState<Boolean> = mutableStateOf(false)

    private val addPackageViewModel: AddPackageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()

        addPackageViewModel.addPackageResult.observe(this@AddPackageActivity, Observer {
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

    @Composable
    fun QRcodeScanner(onPackageScanned: (`package`: Package) -> Unit) {

        val scanLauncher = getScanLauncher(this) {
            it ?: return@getScanLauncher
            onPackageScanned(it)
        }

        val scanOptions: ScanOptions = setupScanOptions()

        HandleCameraPermission(this) { isGranted ->
            if (isGranted) {
                scanLauncher.launch(scanOptions)
            } else {
                finish()
                // TODO handle permissions gracefully
            }
        }

        ScannerScreen(
            backAction = { finish() },
            scanAction = { scanLauncher.launch(scanOptions) },
            label = "Enregistrement"
        )
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

