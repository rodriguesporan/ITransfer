package com.rodriguesporan.itransfer.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesporan.itransfer.network.TransactionDatabaseService
import kotlinx.coroutines.launch

class AppViewModel: ViewModel() {

    private val transactionDatabaseService = TransactionDatabaseService()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _transactions = MutableLiveData<MutableList<Transaction>>()
    val transactions: LiveData<MutableList<Transaction>> get() = _transactions

    init {
        getTransactions()
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun addTransaction(transaction: Transaction) {
        _transactions.value?.add(transaction)
    }

    fun addTransactions(transactions: MutableList<Transaction>) {
        _transactions.value?.addAll(transactions)
    }

    private fun getTransactions() {
        _transactions.value = mutableListOf()
//        _transactions.value = transactionDatabaseService.readTransactions("")
//        viewModelScope.launch {
//            _transactions.value = mutableListOf(Transaction("-MYl1arFQIbtdrrolCO1", 349.99, "-MYl-NTXttZTSkYnB8c3"))
//        }
    }
}