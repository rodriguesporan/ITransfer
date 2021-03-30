package com.example.emptyactivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emptyactivity.databinding.FragmentBlankBinding
import com.example.emptyactivity.model.AppViewModel

class BlankFragment : Fragment() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentBlankBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = this@BlankFragment
            blankFragment = this@BlankFragment
            appViewModel = viewModel
        }
        binding?.appViewModel?.setContent(getString(R.string.hello_world))

        return binding.root
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