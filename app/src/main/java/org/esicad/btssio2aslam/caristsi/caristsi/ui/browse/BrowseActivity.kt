package org.esicad.btssio2aslam.caristsi.caristsi.ui.browse

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.ui.theme.CaristSITheme
import org.esicad.btssio2aslam.caristsi.caristsi.ui.HomeScaffold

class BrowseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        

        setContent {
            CaristSITheme {
                // A surface container using the 'background' color from the theme
                HomeScaffold(backAction = { finish() }, title = "Liste des colis") {

                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {

    val packages: List<Package> = listOf(

            Package(
                idPackage = 1234,
                packageNumber = "ABC123",
                productReference = "PRD-XYZ"
            ),
            Package(
                idPackage = 1234,
                packageNumber = "ABC123",
                productReference = "PGF-HML"
            ),
            Package(
                idPackage = 1234,
                packageNumber = "ABC123",
                productReference = "PDF-XML"
            ),

    )

    CaristSITheme {
        HomeScaffold(backAction = { /*TODO*/ }, title = "Liste des colis") {
            PackageList(packages = packages)
        }

    }
}

@Composable
fun PackageList(packages: List<Package>) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        packages.forEach { p: Package ->
            PackageItem(p = p)
        }
    }
}

@Composable
fun PackageItem(p: Package) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(modifier = Modifier.clickable(onClick = {  }).padding(16.dp)) {

            Image(
                painter = painterResource(id = R.drawable.resource_package),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "Référence du produit : ${p.productReference}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Description : ${p.packageNumber}"
                )
            }
        }
    }
}