package org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

@AndroidEntryPoint
class WareHouseComposableActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wareHouseViewModel: WareHouseViewModel = ViewModelProvider(this)[WareHouseViewModel::class.java]

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
                        }, floatingActionButton = {
                            LargeFloatingActionButton(onClick = { wareHouseViewModel.loadPackages() }) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Rafraîchir la liste des colis",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
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

    @Composable
    @Preview
    fun PackageCardPreview() {
        CaristSITheme {
            PackageCard(
                Package(
                    1,
                    "number fictif",
                    "article Ref",
                    "article description",
                ),
                showPackage = {}
            )
        }
    }
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        @Preview(showBackground = true)
        fun PackageListPreview() {
            CaristSITheme {

                val packages: List<Package> = listOf(
                    Package(1, "number fictif", "article Ref", "article description"),
                    Package(2, "autre number fictif", "article Ref 2", "article description 2")
                )

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
                    }, floatingActionButton = {
                        LargeFloatingActionButton(onClick = {}) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Rafraîchir la liste des colis",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(packages) { aPackage ->
                                PackageCard(aPackage, showPackage = {})
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PackagesColumn(wareHouseViewModel: WareHouseViewModel = viewModel()) {

        val packageModalState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showPackageModal by remember { mutableStateOf(false) }
        var selectedPackage by remember { mutableStateOf<Package?>(null) }


        // on récupère la valeur du viewModel en paramètre
        val state = wareHouseViewModel.packagesState.observeAsState(listOf())

        Log.i("WareHouseActivity", "State : ${state.value.size} colis")


        // Si la liste est vide - on affiche un spinner de chargement
        if (state.value.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.value) { aPackage ->
                    PackageCard(aPackage, showPackage = {
                            showPackageModal = true; selectedPackage = aPackage
                    })

                }
            }
            if (showPackageModal && selectedPackage != null) {
                PackageModal(selectedPackage!!, onDismiss = { showPackageModal = false }, sheetState = packageModalState)
            }
        }
    }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun PackageModal(`package`: Package, onDismiss: (Package) -> Unit, sheetState: SheetState) {
        ModalBottomSheet(onDismissRequest = { onDismiss(`package`) }, sheetState = sheetState) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(`package`.description ?: "Pas de description")
            }
        }

    }

    class PackagePreviewProvider : PreviewParameterProvider<List<Package>> {
        override val values: Sequence<List<Package>> = sequenceOf(
            listOf(
                Package(1, "number fictif", "article Ref", "article description"),
                Package(2, "autre number fictif", "article Ref 2", "article description 2")
            )
        )
    }

}