package org.esicad.btssio2aslam.caristsi.caristsi.data

import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PackageService {
    @GET("/packages")
    suspend fun getPackages(): List<Package>

    @POST("/package")
    suspend fun addPackage(@Body `package`: Package): Response<Any>
}