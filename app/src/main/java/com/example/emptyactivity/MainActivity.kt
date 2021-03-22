package com.example.emptyactivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.emptyactivity.databinding.ActivityMainBinding
import com.example.emptyactivity.model.AppViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.apply {
            lifecycleOwner = this@MainActivity
            appViewModel = model
            mainActivity = this@MainActivity
        }
        binding?.appViewModel?.setContent(getString(R.string.hello_world))
    }

    fun sendEmail() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_msg))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            type = "text/plain"
        }

        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d("INTENT", e.toString())
        }
    }
}