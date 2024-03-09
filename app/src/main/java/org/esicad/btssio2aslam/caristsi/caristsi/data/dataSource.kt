package org.esicad.btssio2aslam.caristsi.caristsi.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.LoggedInUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object ApiClient {
    private const val BASE_URL =
        "http://192.168.1.100:8080"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    interface LoginService {
        @POST("login")
        suspend fun login(@Body params:Map<String,String>): LoggedInUser
    }
    val loginService :  LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }
}



