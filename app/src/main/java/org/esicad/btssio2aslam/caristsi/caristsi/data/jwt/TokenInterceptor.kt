package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt


import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Classe responsable d'intercepter les requêtes HTTP et de gérer l'authentification via JWT
 */
class TokenInterceptor @Inject constructor(
    // on injecte le JwtTokenManager à partir de notre mécanisme de DI
    private val tokenManager: JwtTokenManager,
) : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        // on récupère le token à partir de notre JwtTokenManager injecté
        val token = runBlocking {
            tokenManager.getAccessJwt()
        }

        val request = chain.request().newBuilder()
                            .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")

        return chain.proceed(request.build())
    }
}