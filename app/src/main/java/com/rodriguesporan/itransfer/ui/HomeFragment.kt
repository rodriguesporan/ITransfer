package com.rodriguesporan.itransfer.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rodriguesporan.itransfer.databinding.FragmentHomeBinding
import com.rodriguesporan.itransfer.data.AppViewModel

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

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    fun goToScanner() = startActivity(Intent(requireContext(), ScanActivity::class.java))
}