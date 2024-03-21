package org.esicad.btssio2aslam.caristsi.caristsi.scan

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsets.Type
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.zxing.integration.android.IntentIntegrator
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme


class QRcodeScannerActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CAMERA = 1

    val scannedPackage: MutableState<Package?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CaristSITheme {
                QRcodeScanner()
            }
        }
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
        } else {
            //initQRCodeScanner()
        }
    }
    private fun initQRCodeScanner() {
        // Initialize QR code scanner here
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setOrientationLocked(false)
        integrator.setPrompt("Scan a QR code")
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required",
                    Toast.LENGTH_LONG
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
                Toast.makeText(this, "Package Scanned !", Toast.LENGTH_LONG).show()
                Log.i("I", "QR code scanned: ${result.contents}")
                resultData.setData(Uri.parse(result.contents))
                // decode json data from qr code

                scannedPackage.value = Gson().fromJson(result.contents, Package::class.java)
                Log.i("I", "Data decoded : ${scannedPackage.value}")


                //setResult(RESULT_OK, resultData)
            }

            //finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Composable
    @Preview
    fun QRcodeScanner() {

        val packages = listOf(
            Package(1, "123456", "1234567890123", "2022-12-31"),
            Package(2, "654321", "3210987654321", "2022-12-31"),
            Package(3, "987654", "9876543210123", "2022-12-31")
        )

        CaristSITheme {
            Box(modifier = Modifier.safeDrawingPadding()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    if (scannedPackage.value != null) {
                        PackageCard(scannedPackage.value!!) {}
                    } else {
                        Text(text = "Scannez un code QR pour commencer")
                    }
                    // PackageCard(packages.first(), {})
                    Button(onClick = { initQRCodeScanner() }) {
                        Text(text = "Scanner un code QR")
                    }
                }
            }
        }
    }

    @Composable
    fun PackageCard(`package`: Package, showPackage: (packageId: Number) -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
            ),
            shape = RoundedCornerShape(22.dp),
            onClick = { showPackage(0) }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Image(
                    modifier = Modifier.width(64.dp),
                    painter = painterResource(R.drawable.resource_package),
                    contentDescription = "Package image"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PackageInfoLabel(label = "Référence", value = `package`.articleReference)
                    PackageInfoLabel(label = "EAN13", value = `package`.packageNumber)
                }
            }
        }

    }

    @Composable
    fun PackageInfoLabel(label: String, value: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = label,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(value)
        }
    }


}