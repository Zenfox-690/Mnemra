package com.example.mnemra.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun HomeScreen(
    viewModel: MemoryViewModel = hiltViewModel()
) {
    Text("Mnemra")
}