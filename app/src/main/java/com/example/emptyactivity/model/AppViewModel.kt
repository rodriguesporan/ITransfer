package com.example.emptyactivity.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    private val _content = MutableLiveData<String>("")
    val content: LiveData<String> get() = _content

    fun setContent(newContent: String) {
        _content.value = newContent
    }
}