package com.rodriguesporan.itransfer.model

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class User(
        var phone: String? = null,
        var QRToken: String? = null,
        var lastName: String? = null,
        var firstName: String? = null,
        var assets: Float? = null,
        var timestamp: Timestamp? = null
)