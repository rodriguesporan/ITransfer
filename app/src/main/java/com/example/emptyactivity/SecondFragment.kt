package com.example.emptyactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.emptyactivity.databinding.FragmentSecondBinding
import com.example.emptyactivity.model.AppViewModel

class SecondFragment : Fragment() {
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@SecondFragment
            secondFragment = this@SecondFragment
            appViewModel = viewModel
        }

        return binding.root
    }
}