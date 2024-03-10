package org.esicad.btssio2aslam.caristsi.caristsi.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL =
        "http://192.168.1.100:8080"

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClientUnauthenticated: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val httpClientAuthenticated: OkHttpClient by lazy {
        OkHttpClient.Builder()
            //.addInterceptor(TokenInterceptor)
     .build()
    }

    /**
     * Client Http Retrofit non authentifié (pour le login)
     */
    private val retrofitUnauthenticated: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClientUnauthenticated)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    private val retrofitAuthenticated: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(httpClientAuthenticated)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    val loginService: LoginService by lazy {
        retrofitUnauthenticated.create(LoginService::class.java)
    }

    val packageService: PackageService by lazy {
        retrofitAuthenticated.create(PackageService::class.java)
    }
}



