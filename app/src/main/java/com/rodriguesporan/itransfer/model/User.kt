package com.rodriguesporan.itransfer.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        var phone: String? = null,
        var QRToken: String? = null,
        var lastName: String? = null,
        var firstName: String? = null,
        var assets: Double? = null,
        var timestamp: Long? = null
)