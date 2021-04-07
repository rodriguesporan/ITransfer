package com.example.emptyactivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.emptyactivity.databinding.FragmentFirstBinding
import com.example.emptyactivity.model.AppViewModel

class FirstFragment : Fragment() {
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@FirstFragment
            firstFragment = this@FirstFragment
            appViewModel = viewModel
        }
        binding.appViewModel?.setContent(getString(R.string.hello_world))

        return binding.root
    }

    fun changeMsg() {
        if (viewModel.content.value.equals(this.getString(R.string.hello_world))) {
            viewModel.setContent(viewModel.secondGreeting.value.toString())
        } else {
            viewModel.setContent(this.getString(R.string.hello_world))
        }
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

    fun goNext() {
        findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
    }
}