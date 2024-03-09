package org.esicad.btssio2aslam.caristsi.caristsi.data

import android.util.Log
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.LoggedInUser
import java.io.IOException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val map = mapOf("login" to username, "password" to password)
            val result = ApiClient.loginService.login(map)
            return Result.Success(result)
        } catch (e: Throwable) {
            e.localizedMessage?.let { Log.e("E", it) }
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}