package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt

interface JwtTokenManager {
    suspend fun saveAccessJwt(token: String)
    suspend fun getAccessJwt(): String?
    suspend fun clearAllTokens()
}