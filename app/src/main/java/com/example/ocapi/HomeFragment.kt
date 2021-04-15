package com.example.ocapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ocapi.databinding.FragmentHomeBinding
import com.example.ocapi.model.AppViewModel

class HomeFragment : Fragment() {
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@HomeFragment
            homeFragment = this@HomeFragment
            appViewModel = viewModel
        }
        binding.appViewModel?.setContent(getString(R.string.hello_world))

        return binding.root
    }

    fun goToScanner() {
    }
}