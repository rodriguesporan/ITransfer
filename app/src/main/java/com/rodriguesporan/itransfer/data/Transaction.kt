package com.rodriguesporan.itransfer.data

import com.google.firebase.database.IgnoreExtraProperties
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@IgnoreExtraProperties
data class Transaction(
        var uid: String? = null,
        var amount: Double? = null,
        var senderId: String? = null,
        var receiverId: String? = null,
        var timestamp: Long? = null
) {
    fun formatTimestamp(): String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timestamp)

    fun formatAmount(): String {
        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.CEILING

        return "R$ ${df.format(amount)}"
    }
}