package com.rodriguesporan.itransfer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.databinding.ActivitySendPaymentBinding

class SendPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_payment)
        binding.sendPaymentActivity = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun pay() = startActivity(Intent(this, PaymentReceiptActivity::class.java))
}