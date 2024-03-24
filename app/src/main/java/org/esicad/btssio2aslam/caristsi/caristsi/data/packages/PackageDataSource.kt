package org.esicad.btssio2aslam.caristsi.caristsi.data.packages

import android.util.Log
import org.esicad.btssio2aslam.caristsi.caristsi.data.ApiClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.Result
import org.esicad.btssio2aslam.caristsi.caristsi.data.jwt.JwtTokenManager
import javax.inject.Inject
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import java.io.IOException

class PackageDataSource @Inject constructor(
    private val api: ApiClient,
    private val tokenManager: JwtTokenManager
) {
    suspend fun addPackage(`package`: Package): Result<Any> {
        return try {
            val result = api.packageService.addPackage(`package`)
            if (result.isSuccessful) {
                Result.Success(result)
            } else {
                Log.e("AddPackageRequest", "HTTP Error --> ${result.code().toString()}")
                throw Exception(result.errorBody()?.string())
            }

        } catch (e: Throwable) {
            e.localizedMessage?.let { Log.e("AddPackageRequest", it) }
            Result.Error(IOException("Error adding package", e))
        }
    }

    suspend fun deletePackage(`package`: Package): Result<Any> {
        return try {
            val result = api.packageService.deletePackage(`package`)
            if (result.isSuccessful) {
                Result.Success(result)
            } else throw Exception("HTTP error")
        } catch (e: Throwable) {
            e.localizedMessage?.let { Log.e("DeletePackageRequest", it) }
            Result.Error(IOException("Error deleting package", e))
        }
    }
}