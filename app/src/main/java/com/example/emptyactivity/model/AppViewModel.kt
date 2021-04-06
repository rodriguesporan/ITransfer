package com.example.emptyactivity.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    private val _content = MutableLiveData<String>("")
    val content: LiveData<String> get() = _content

    private val _secondGreeting = MutableLiveData<String>("Bye World")
    val secondGreeting: LiveData<String> get() = _secondGreeting

    fun setContent(newValue: String) {
        _content.value = newValue
    }

    fun setSecondGreeting(newValue: String) {
        _secondGreeting.value = newValue
    }
}