package com.rodriguesporan.itransfer.data

import com.google.firebase.database.IgnoreExtraProperties
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class User(
        var uid: String? = null,
        var googleUid: String? = null,
        var displayName: String? = null,
        var email: String? = null,
        var phoneNumber: String? = null,
        var photoUrl: String? = null,
        var total: Double? = null,
        var updatedAt: Date? = null,
        var lastWeekTransactionsUid: ArrayList<String> = arrayListOf()
) {
    fun formatUpdatedAt(): String {
        return if (updatedAt == null) {
            ""
        } else {
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updatedAt)
        }
    }

    fun formatAmount(): String {
        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.CEILING

        return if (total == null) { "0.00" } else { df.format(total!!) }
    }
}