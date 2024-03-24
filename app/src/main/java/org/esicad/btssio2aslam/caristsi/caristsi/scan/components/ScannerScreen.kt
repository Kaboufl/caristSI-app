package org.esicad.btssio2aslam.caristsi.caristsi.scan.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ScannerScreen(
    label: String = "Scanner de colis",
    backAction: () -> Unit = {},
    scanAction: () -> Unit = {}
) {
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