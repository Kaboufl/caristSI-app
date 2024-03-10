package org.esicad.btssio2aslam.caristsi.caristsi.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Package (
    val idPackage: Number,
    val packageNumber: String,
    val productReference: String
)