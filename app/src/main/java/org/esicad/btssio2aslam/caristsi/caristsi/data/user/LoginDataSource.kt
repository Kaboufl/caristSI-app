package org.esicad.btssio2aslam.caristsi.caristsi.data.user

import android.util.Log
import org.esicad.btssio2aslam.caristsi.caristsi.data.ApiClient
import org.esicad.btssio2aslam.caristsi.caristsi.data.Result
import org.esicad.btssio2aslam.caristsi.caristsi.data.jwt.JwtTokenManager
import org.esicad.btssio2aslam.caristsi.caristsi.data.model.LoggedInUser
import java.io.IOException
import javax.inject.Inject


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val api : ApiClient, private val tokenManager: JwtTokenManager ) {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val map = mapOf("login" to username, "password" to password)
            val result = api.loginService.login(map)
            Result.Success(result)
        } catch (e: Throwable) {
            e.localizedMessage?.let { Log.e("E", it) }
            Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun logout() {
        tokenManager.clearAllTokens()
    }
}