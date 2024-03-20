package org.esicad.btssio2aslam.caristsi.caristsi.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.scan.QRcodeScannerActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.login.LoginActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.warehouse.WareHouseComposableActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.theme.CaristSITheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CaristSITheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 40.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.resource_package),
                            contentDescription = "",
                            modifier = Modifier.width(280.dp)
                        )
                        Text(text = "Carist SI 2.0", style = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp
                        ))
                        Button(
                            modifier = Modifier.padding(top = 120.dp),
                            onClick = {
                                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Text("Se connecter")
                        }
                    }
                }
            }
        }

    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val storePackageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.i("I", result.data?.data.toString())
                }
            }
        val buttonCamera = findViewById<Button>(R.id.button_store_package)
        buttonCamera.setOnClickListener { _ ->
            // start new activity
            storePackageIntentLauncher.launch(Intent(this, QRcodeScannerActivity::class.java))
        }

        val buttonShowPackages = findViewById<Button>(R.id.button_show_packages_list)
        buttonShowPackages.setOnClickListener { _ ->
            startActivity(Intent(this, WareHouseComposableActivity::class.java))
        }
        val buttonLogout = findViewById<Button>(R.id.button_logout)
        buttonLogout.setOnClickListener { _ ->
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(
                applicationContext,
                "Vous avez été déconnecté",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    } */
}

@Composable
@Preview(showBackground = false)
fun Home() {
    Surface(
        color = Color.White,
        contentColor = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello, world!")

            Button(onClick = {  }) {
                Text("Se connecter")
            }
        }
    }
}