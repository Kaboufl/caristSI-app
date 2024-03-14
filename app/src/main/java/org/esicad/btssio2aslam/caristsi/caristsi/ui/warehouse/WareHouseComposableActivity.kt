package org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.esicad.btssio2aslam.caristsi.caristsi.data.ApiClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse.ui.theme.CaristSITheme
import javax.inject.Inject

@AndroidEntryPoint
class WareHouseComposableActivity : ComponentActivity() {
    private lateinit var packages: Array<Package>

    @Inject
    lateinit var api: ApiClient

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            packages = api.packageService.getPackages()
        }

        setContent {
            CaristSITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Liste des colis") },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                            )
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            PackagePreview(packages.asList())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PackageComponent(`package`: Package) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 8.dp)
    ) {
        Text(`package`.packageNumber)
        Text(`package`.articleReference)
    }
    Divider()
}

private class PackagePreviewProvider : PreviewParameterProvider<List<Package>> {
    override val values: Sequence<List<Package>> = sequenceOf(
        listOf(
            Package(1, "number fictif", "article Ref"),
            Package(2, "autre number fictif", "article Ref 2")
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PackagePreview(@PreviewParameter(PackagePreviewProvider::class) packages: List<Package>) {
    LazyColumn {
        items(packages) { aPackage ->
            PackageComponent(aPackage)
        }
    }
}