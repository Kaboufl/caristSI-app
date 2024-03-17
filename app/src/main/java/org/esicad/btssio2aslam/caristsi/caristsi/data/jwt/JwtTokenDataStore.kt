package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) :
    JwtTokenManager {

    companion object {
        val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
    }

    override suspend fun saveAccessJwt(token: String) {
        Log.i("I", "saveAccessJwt : $token")
        dataStore.edit { preferences ->
            preferences[ACCESS_JWT_KEY] = token
        }
    }

    override suspend fun getAccessJwt(): String? {
        val token =
            dataStore.data.map { preferences ->
                preferences[ACCESS_JWT_KEY]
            }.first()
        Log.i("I", "getAccessJwt : $token")
        return token
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_JWT_KEY)
        }
    }
}