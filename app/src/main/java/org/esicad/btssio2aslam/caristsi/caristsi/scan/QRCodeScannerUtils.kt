package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package

@Composable
fun getScanLauncher(
    activity: ComponentActivity,
    onScanResult: (Package?) -> Unit
) = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
    if (result.contents != null) {
        try {
            val scannedPackage: Package =
                Gson().fromJson(result.contents, Package::class.java)
            onScanResult(scannedPackage)
        } catch (e: JsonSyntaxException) {
            Log.e(
                "QRCodeScanner",
                "Impossible de décoder les données du colis",
                e
            )
            Toast.makeText(
                activity.applicationContext,
                "Colis invalide !",
                Toast.LENGTH_SHORT
            ).show()
            onScanResult(null)
        } catch (e: Exception) {
            Log.e("QRCodeScanner", "Erreur interne", e)
            Toast.makeText(
                activity.applicationContext,
                "Erreur interne",
                Toast.LENGTH_SHORT
            ).show()
            onScanResult(null)
        }
    } else {
        Log.i("QRCodeScanner", "scan cancelled")
        onScanResult(null)
    }
}

@Composable
fun setupScanOptions(): ScanOptions {
    return ScanOptions()
        .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        .setPrompt(stringResource(R.string.package_scan_label))
        .setBeepEnabled(false)
        .setOrientationLocked(false)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleCameraPermission(activity: ComponentActivity, onPermissionResult: (Boolean) -> Unit) {
    val packageName = activity.packageName
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(cameraPermissionState) {
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            Toast.makeText(
                activity.applicationContext,
                "Camera permission is required",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(activity.baseContext, intent, null)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}