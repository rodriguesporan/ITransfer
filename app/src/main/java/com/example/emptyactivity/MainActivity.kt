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
        binding.lifecycleOwner = this
        binding.appViewModel = model
        binding.mainActivity = this
    }

    fun sendEmail() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "André")
            putExtra(Intent.EXTRA_EMAIL, "andre@gmail")
            putExtra(Intent.EXTRA_SUBJECT, "You have been hired")
            type = "text/plain"
        }

        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d("INTENT", e.toString())
        }
    }
}