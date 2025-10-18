package com.raf.mobiletaskcodeidtest.home.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorItemView(modifier: Modifier = Modifier, message: String? = null) {
    Text(
        text = message ?: "Something went wrong",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}