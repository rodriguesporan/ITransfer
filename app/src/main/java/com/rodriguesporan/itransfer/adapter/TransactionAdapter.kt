package com.rodriguesporan.itransfer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodriguesporan.itransfer.databinding.FragmentStatementItemBinding
import com.rodriguesporan.itransfer.model.Transaction

class TransactionAdapter(): ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DiffCallback) {

    class TransactionViewHolder(private val binding: FragmentStatementItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.transaction = transaction
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem.amount == newItem.amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(FragmentStatementItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) = holder.bind(getItem(position))
}