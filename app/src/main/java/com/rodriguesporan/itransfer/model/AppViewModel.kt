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

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    init {
        getTransactions()
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun setTransactions(transactions: List<Transaction>) {
        _transactions.value = transactions
    }

    private fun getTransactions() {
//        _transactions.value = transactionDatabaseService.readTransactions("")
        viewModelScope.launch {
            _transactions.value = listOf(Transaction("-MYl1arFQIbtdrrolCO1", 349.99, "-MYl-NTXttZTSkYnB8c3"))
        }
    }
}