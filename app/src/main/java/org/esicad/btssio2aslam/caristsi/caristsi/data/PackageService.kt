package org.esicad.btssio2aslam.caristsi.caristsi.data

import org.esicad.btssio2aslam.caristsi.caristsi.data.model.Package
import retrofit2.http.GET

interface PackageService {
    @GET
    fun getPackages(): Array<Package>
}