package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Observer
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.DeletePackageDialog
import org.esicad.btssio2aslam.caristsi.caristsi.scan.components.ScannerScreen

@AndroidEntryPoint
class DeletePackageActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CAMERA = 1

    val showDeletePackageModal: MutableState<Boolean> = mutableStateOf(false)

    private val deletePackageViewModel: DeletePackageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deletePackageViewModel.delPackageResult.observe(this@DeletePackageActivity, Observer {
            val deleteResult = it ?: return@Observer

            if (deleteResult.error != null) {
                Toast.makeText(
                    applicationContext,
                    "Impossible de déstocker le colis",
                    Toast.LENGTH_LONG
                ).show()
            }

            if (deleteResult.success != null) {
                Toast.makeText(
                    applicationContext,
                    "Colis déstocké",
                    Toast.LENGTH_LONG
                ).show()
            }

            showDeletePackageModal.value = false
        })

        setContent {
            CaristSITheme {

                QRCodeScanner(
                    onPackageScanned = { scannedPackage ->
                        Log.i("PackageScanner", "package scanné : ${scannedPackage.toString()}")
                        deletePackageViewModel.setPackage(scannedPackage)
                        showDeletePackageModal.value = true
                    }
                )

                if (showDeletePackageModal.value) {
                    deletePackageViewModel.delPackage.value?.let {
                        DeletePackageDialog(
                            `package` = it,
                            onDismissRequest = { showDeletePackageModal.value = false },
                            onConfirmRequest = { packageToDelete ->
                                deletePackageViewModel.delete(packageToDelete)
                            }
                        )
                    }
                }

            }
        }
    }

    @Composable
    fun QRCodeScanner(onPackageScanned: (`package`: Package) -> Unit) {

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

        ScannerScreen(backAction = { finish() }, scanAction = { scanLauncher.launch(scanOptions) })
    }
}
