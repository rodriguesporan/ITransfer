package com.rodriguesporan.itransfer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodriguesporan.itransfer.databinding.FragmentItemBinding
import com.rodriguesporan.itransfer.model.AppViewModel
import com.rodriguesporan.itransfer.model.Transaction

class MyItemRecyclerViewAdapter(): ListAdapter<Transaction, MyItemRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: FragmentItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.transaction = transaction

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem.amount == newItem.amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }
}