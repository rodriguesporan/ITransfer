package com.rodriguesporan.itransfer.data

import com.google.firebase.database.IgnoreExtraProperties
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class Transaction(
        var uid: String? = null,
        var amount: Double? = null,
        var senderUid: String? = null,
        var receiverUid: String? = null,
        var createdAt: Date? = null,
        var usersUid: ArrayList<String> = arrayListOf()
) {
    fun formatCreatedAt(): String {
        return if (createdAt == null) {
            ""
        } else {
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(createdAt)
        }
    }

    fun formatAmount(): String {
        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.CEILING

        return if (amount == null) { "0.00" } else { df.format(amount!!) }
    }
}