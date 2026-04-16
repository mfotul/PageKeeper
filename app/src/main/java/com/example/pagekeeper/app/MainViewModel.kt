package com.example.pagekeeper.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagekeeper.pages.domain.library.PageDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pageDataSource: PageDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(0)
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.value = pageDataSource.checkPoint()
        }
    }
}
