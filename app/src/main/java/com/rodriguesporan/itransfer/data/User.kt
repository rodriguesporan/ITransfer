package com.rodriguesporan.itransfer.data

import com.google.firebase.database.IgnoreExtraProperties
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@IgnoreExtraProperties
data class User(
        var uid: String? = null,
        var phone: String? = null,
        var QRToken: String? = null,
        var lastName: String? = null,
        var firstName: String? = null,
        var assets: Double? = null,
        var timestamp: Long? = null
) {
    fun formatTimestamp(): String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timestamp)

    fun formatAmount(): String {
        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.CEILING

        return df.format(assets)
    }
}