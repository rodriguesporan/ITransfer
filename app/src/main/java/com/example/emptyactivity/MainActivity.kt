package com.example.emptyactivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.emptyactivity.databinding.ActivityMainBinding
import com.example.emptyactivity.model.AppViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.content.observe(this, Observer<String> { newString ->
            binding.txtView.text = newString
        })

        binding.showMsg.setOnClickListener {
            model.setContent("By World")
            Toast.makeText(this, model.content.value, Toast.LENGTH_LONG).show()
        }

        binding.sendEmail.setOnClickListener {
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
}