package org.esicad.btssio2aslam.caristsi.caristsi.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import okhttp3.OkHttpClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.jwt.TokenInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

private const val BASE_URL =
    "http://192.168.1.100:8080"
@Module
class ApiClient() {
    @Inject lateinit var interceptor: TokenInterceptor

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
    /**
     * Instance de Retrofit
     */
    private val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    val loginService: LoginService by lazy {
        retrofitInstance.create(LoginService::class.java)
    }

    val packageService: PackageService by lazy {
        retrofitInstance.create(PackageService::class.java)
    }
}



