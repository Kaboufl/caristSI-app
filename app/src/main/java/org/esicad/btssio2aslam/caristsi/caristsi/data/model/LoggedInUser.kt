package org.esicad.btssio2aslam.caristsi.caristsi.data.model

/**
 * Data class that describes user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val idCarist: String,
    val nomCarist: String,
    val prenomCarist: String,
    val loginCarist: String
)