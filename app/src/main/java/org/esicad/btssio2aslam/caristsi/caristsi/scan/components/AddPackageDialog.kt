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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPackageDialog(`package`: Package, onDismissRequest: () -> Unit, onConfirmRequest: (`package`: Package) -> Unit = {}) {

    var description by remember {
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
                    text = "Ajouter un colis",
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Référence : ")
                    `package`?.articleReference?.let { Text(it) }

                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Identifiant : ")
                    `package`?.packageNumber?.let { Text(it) }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text("Description")
                    },
                    maxLines = 3,
                    modifier = Modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    shape = RoundedCornerShape(16.dp),

                    )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { onDismissRequest() }) {
                        Text("Annuler")
                    }
                    Button(onClick = {
                        val newPackage: Package = Package(
                            0,
                            `package`.packageNumber,
                            `package`.articleReference,
                            description
                        )
                        onConfirmRequest(newPackage)
                    }) {
                        Text("Ajouter")
                    }
                }

            }

        }
    }
}