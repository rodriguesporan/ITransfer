package com.rodriguesporan.itransfer.model

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Transaction(
        var amount: Double? = null,
        var senderId: String? = null,
        var receiverId: String? = null,
        var timestamp: Timestamp? = null
)