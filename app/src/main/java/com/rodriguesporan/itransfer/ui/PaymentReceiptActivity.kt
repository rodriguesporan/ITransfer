package com.rodriguesporan.itransfer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rodriguesporan.itransfer.R

class PaymentReceiptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_receipt)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}