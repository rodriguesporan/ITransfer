package com.rodriguesporan.itransfer.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    fun setUser(user: User) {
        _user.value = user
    }

    fun setTransactions(transactions: List<Transaction>) {
        _transactions.value = transactions
    }
}