package org.esicad.btssio2aslam.caristsi.caristsi.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.esicad.btssio2aslam.caristsi.caristsi.R
import org.esicad.btssio2aslam.caristsi.caristsi.ui.scan.QRcodeScannerActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.BrowsePackages

class HomeActivityOLD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonCamera = findViewById<Button>(R.id.button_store_package)
        buttonCamera.setOnClickListener {_ ->
            // start new activity
            val intent = Intent(this, QRcodeScannerActivity::class.java)
            startActivity(intent)

        }

        val buttonBrowsePackages = findViewById<Button>(R.id.browse_packages)
        buttonBrowsePackages.setOnClickListener {_ ->
            val intent = Intent(this, BrowsePackages::class.java)
            startActivity(intent)
        }
    }
}