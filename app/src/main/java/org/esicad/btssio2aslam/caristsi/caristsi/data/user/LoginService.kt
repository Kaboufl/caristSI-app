package org.esicad.btssio2aslam.caristsi.caristsi.data.user

import org.esicad.btssio2aslam.caristsi.caristsi.data.model.LoggedInUser
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body params:Map<String,String>): LoggedInUser
}