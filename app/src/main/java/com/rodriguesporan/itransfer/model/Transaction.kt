package com.rodriguesporan.itransfer.model

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Transaction(
        var uid: String? = null,
        var amount: Double? = null,
        var senderId: String? = null,
        var receiverId: String? = null,
        var timestamp: Long? = null
)