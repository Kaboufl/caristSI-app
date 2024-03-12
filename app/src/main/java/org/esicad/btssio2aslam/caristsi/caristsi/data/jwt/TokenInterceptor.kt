package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt


import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/*
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val JWT_TOKEN_STORED = stringPreferencesKey("jwt_token")
val exampleCounterFlow: Flow<String> = dataStore.data
    .map { preferences ->
        // No type safety.
        preferences[JWT_TOKEN_STORED] ?: 0
    }
*/

/**
 * Classe responsable d'intercepter les requêtes HTTP et de gérer l'authentification via JWT
 */
@Singleton
class TokenInterceptor @Inject constructor(
    // on injecte le JwtTokenManager à partir de notre mécanisme de DI
    val tokenManager: JwtTokenManager,
) : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // si l'URL contient "login", on gère le stockage du token renvoyé
        if (request.url().encodedPath().contains("/login") && request.method().equals("post")) {
            val response = chain.proceed(request)
            val token = response.header(HEADER_AUTHORIZATION)
            if (token != null && token.isNotBlank()) {
                runBlocking { tokenManager.saveAccessJwt(token.drop(TOKEN_TYPE.length+1)) }
            }
            // on retourne la valeur originale de la requête
            return response
        } else {
            // Si l'URL ne contient pas "login", on récupère le token et on l'injecte
            val token = runBlocking {
                tokenManager.getAccessJwt()
            }
            // on "fabrique" une copie de la requête originale et on la décore d'un nouvel header
            val rebuild = chain.request().newBuilder()
            rebuild.addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
            // on renvoie la réponse produite par la requête
            return chain.proceed(rebuild.build())
        }
    }
}