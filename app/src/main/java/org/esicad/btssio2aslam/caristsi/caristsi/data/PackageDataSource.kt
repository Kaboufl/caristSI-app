package org.esicad.btssio2aslam.caristsi.caristsi.data

import android.util.Log
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import java.io.IOException

class PackageDataSource {
    suspend fun getPackages(): Result<Array<Package>> {
        try {
            val result = ApiClient.packageService.getPackages()
            return Result.Success(result)
        } catch (e: Throwable) {
            e.localizedMessage?.let { Log.e("E", it) }
            return Result.Error(IOException("Erreur lors de la récpération des colis", e))
        }
    }
}