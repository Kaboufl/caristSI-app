package org.esicad.btssio2aslam.caristsi.caristsi.data.packages

import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface PackageService {
    @GET("/packages")
    suspend fun getPackages(): List<Package>

    @POST("/package")
    suspend fun addPackage(@Body `package`: Package): Response<Any>

    @DELETE("/package")
    suspend fun deletePackage2(@Body `package`: Package): Response<Any>

    @HTTP(method = "DELETE", path = "/package", hasBody = true)
    suspend fun deletePackage(@Body `package`: Package): Response<Any>
}