package org.esicad.btssio2aslam.caristsi.caristsi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

val samplePackage: Package = Package(
    1,
    "number fictif",
    "article Ref",
    "article description",
)
@Preview
@Composable
fun PreviewPackageCard() {
    CaristSITheme {
        PackageCard(`package` = samplePackage, showPackage = {})
    }
}

@Preview
@Composable
fun PreviewPackageCardWithDescription() {
    CaristSITheme {
        PackageCard(`package` = samplePackage, showPackage = {}) {
            PackageDescription(packageDescription = samplePackage.description)
        }
    }
}

@Composable
fun PackageCard(`package`: Package, showPackage: (packageId: Number) -> Unit, content:  @Composable ColumnScope.() -> Unit = {}) {
    val context = currentCompositionLocalContext
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(22.dp),
        onClick = { showPackage(`package`.idPackage) }
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
                PackageInfoLabel(label = stringResource(R.string.package_ref_label), value = `package`.articleReference)
                PackageInfoLabel(label = stringResource(R.string.package_id_label), value = `package`.packageNumber)
            }
        }
        content()
    }
}

@Composable
fun PackageInfoLabel(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
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

@Composable
fun PackageDescription(packageDescription: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.tertiaryContainer),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.package_desc_label),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            modifier = Modifier.padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        )
        Text(
            text = packageDescription,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            modifier = Modifier.padding(horizontal = 12.dp)
                .padding(top = 4.dp, bottom = 8.dp)
        )
    }
}