package com.example.emptyactivity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnShowMsg = findViewById<Button>(R.id.show_msg)
        btnShowMsg.setOnClickListener { _ ->
            Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show()
        }
    }
}