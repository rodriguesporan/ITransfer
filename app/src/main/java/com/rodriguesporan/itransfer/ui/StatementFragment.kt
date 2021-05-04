package com.rodriguesporan.itransfer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodriguesporan.itransfer.adapter.TransactionAdapter
import com.rodriguesporan.itransfer.databinding.FragmentStatementBinding
import com.rodriguesporan.itransfer.data.AppViewModel

class StatementFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatementBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@StatementFragment
            appViewModel = viewModel
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = TransactionAdapter()
        }

        return binding.root
    }
}