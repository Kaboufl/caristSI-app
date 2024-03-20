package org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@AndroidEntryPoint
class WareHouseComposableActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                                title = {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { finish() }, modifier = Modifier.padding(
                                                end = 16.dp
                                            )
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Retour"
                                            )

                                        }
                                        Text("Liste des colis")
                                    }

                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                            )
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            PackagesColumn()
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
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            Text(`package`.packageNumber)
            Text(`package`.articleReference)
        }
        HorizontalDivider()
    }


    @Composable
    fun PackagesColumn(wareHouseViewModel: WareHouseViewModel = viewModel()) {
        // on récupère la valeur du viewModel en paramètre
        val state = wareHouseViewModel.packagesState.observeAsState(listOf())
        Log.i("warehouseactivity", "state value " + state.value.toString())
        // Si la liste est vide - on affiche un spinner de chargement
        if (state.value.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            // on déclenche le chargement des données dans le viewModel
            wareHouseViewModel.loadPackages()
        } else {
            // la liste contient des éléments => on va utiliser notre PackageComponent
            LazyColumn {
                items(state.value) { aPackage ->
                    PackageComponent(aPackage)
                }
            }
        }
    }

    class PackagePreviewProvider : PreviewParameterProvider<List<Package>> {
        override val values: Sequence<List<Package>> = sequenceOf(
            listOf(
                Package(1, "number fictif", "article Ref"),
                Package(2, "autre number fictif", "article Ref 2")
            )
        )
    }

    @Preview
    @Composable
    fun PackagesColumnPreview(@PreviewParameter(PackagePreviewProvider::class) packages: List<Package>) {
        LazyColumn {
            items(packages) { aPackage ->
                PackageComponent(aPackage)
            }
        }
    }

}