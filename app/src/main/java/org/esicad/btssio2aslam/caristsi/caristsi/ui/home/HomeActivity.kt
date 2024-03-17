package org.esicad.btssio2aslam.caristsi.caristsi.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.esicad.btssio2aslam.caristsi.caristsi.ui.HomeScaffold
import org.esicad.btssio2aslam.caristsi.caristsi.ui.browse.BrowseActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.home.ui.theme.CaristSITheme
import org.esicad.btssio2aslam.caristsi.caristsi.ui.scan.QRcodeScannerActivity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaristSITheme {
                // A surface container using the 'background' color from the theme
                HomeScaffold(
                    backAction = { finish() },
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(this, QRcodeScannerActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Scanner un colis")
                    }
                    Button(
                        onClick = {

                          },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Déstocker un colis")
                    }
                    Button(
                        onClick = {
                            val intent = Intent(this, BrowseActivity::class.java)
                            startActivity(intent)
                          },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Consulter les colis")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    CaristSITheme(darkTheme = true) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
                // .padding(all = 20.dp),
            color = Color.Transparent
        ) {
            HomeScaffold(
                backAction = {  },
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Scanner un colis")
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Entreposer un colis")
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Chercher un colis")
                }
            }

        }
    }
}