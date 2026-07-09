package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mnemra.data.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel()