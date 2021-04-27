package com.rodriguesporan.itransfer.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodriguesporan.itransfer.data.Transaction

@BindingAdapter("listAdapter")
fun bindingRecyclerView(recyclerView: RecyclerView, data: List<Transaction>) {
    val adapter = recyclerView.adapter as TransactionAdapter
    adapter.submitList(data)
}