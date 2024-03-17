package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt

import javax.inject.Singleton

@Singleton
interface JwtTokenManager {
    suspend fun saveAccessJwt(token: String)
    suspend fun getAccessJwt(): String?
    suspend fun clearAllTokens()
}