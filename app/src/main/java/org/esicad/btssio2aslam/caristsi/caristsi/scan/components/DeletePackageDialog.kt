package org.esicad.btssio2aslam.caristsi.caristsi.scan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@Preview(showBackground = true)
@Composable
fun PreviewDeletePackageDialog() {
    val `package` = Package(
        idPackage = 0,
        packageNumber = "453226997",
        articleReference = "ecd76e99-9c64-4470-b91d-cd9f4021770b",
        description = "Test package"
    )
    CaristSITheme {
        DeletePackageDialog(
            `package` = `package`,
            onDismissRequest = {},
            onConfirmRequest = {}
        )
    }
}

@Composable
fun DeletePackageDialog(
    `package`: Package,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (`package`: Package) -> Unit = {}
) {

    val description by remember {
        mutableStateOf("")
    }




    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(400.dp),
            shape = RoundedCornerShape(16.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Déstocker le colis ${`package`.articleReference} ?",
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

                PackageCard(`package` = `package`, showPackage = { }, showIcon = false)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { onDismissRequest() }) {
                        Text("Annuler")
                    }
                    Button(onClick = {
                        val newPackage = Package(
                            0,
                            `package`.packageNumber,
                            `package`.articleReference,
                            description
                        )
                        onConfirmRequest(newPackage)
                    }) {
                        Text("Déstocker")
                    }
                }

            }

        }
    }
}
