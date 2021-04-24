package com.rodriguesporan.itransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rodriguesporan.itransfer.databinding.FragmentStatementBinding
import com.rodriguesporan.itransfer.model.AppViewModel

class StatementFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatementBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
//        binding.list.layoutManager = LinearLayoutManager(context)

        /*binding.list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }*/

//        binding.list.adapter = ItemAdapter()

        return binding.root
    }
}