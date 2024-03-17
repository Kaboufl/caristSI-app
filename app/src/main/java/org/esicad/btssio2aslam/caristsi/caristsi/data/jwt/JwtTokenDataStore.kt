package org.esicad.btssio2aslam.caristsi.caristsi.data.jwt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class JwtTokenDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) :
    JwtTokenManager {

    companion object {
        val ACCESS_JWT_KEY = stringPreferencesKey("access_jwt")
    }

    override suspend fun saveAccessJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_JWT_KEY] = token
        }
    }

    override suspend fun getAccessJwt(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_JWT_KEY]
        }.first()
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_JWT_KEY)
        }
    }
}