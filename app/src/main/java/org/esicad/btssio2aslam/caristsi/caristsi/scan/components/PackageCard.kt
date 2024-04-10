package org.esicad.btssio2aslam.caristsi.caristsi.scan.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@Preview(showBackground = true)
@Composable
fun PreviewPackageCard() {
    val `package` = Package(
        idPackage = 0,
        packageNumber = "453226997",
        articleReference = "ecd76e99-9c64-4470-b91d-cd9f4021770b",
        description = "Test package"
    )
    CaristSITheme {
        PackageCard(
            `package` = `package`,
            showPackage = {},
        )
    }
}

@Composable
fun PackageCard(
    `package`: Package,
    showPackage: (packageId: Number) -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ), elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp,
    ), shape = RoundedCornerShape(22.dp), onClick = { showPackage(0) }) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            if (showIcon) {
                Image(
                    modifier = modifier.width(64.dp),
                    painter = painterResource(R.drawable.resource_package),
                    contentDescription = "Package image"
                )
                Spacer(modifier = modifier.width(12.dp))
            }
            Column(
                modifier = modifier.padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PackageInfoLabel(label = "Référence", value = `package`.articleReference)
                PackageInfoLabel(label = "EAN13", value = `package`.packageNumber)
            }
        }
    }

}