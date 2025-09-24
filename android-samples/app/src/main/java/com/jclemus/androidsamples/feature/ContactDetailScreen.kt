package com.jclemus.androidsamples.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun ContactDetailScreen(contactId: Long, viewModel: ContactViewModel, onBackClick: () -> Boolean) {
    val contact = remember(contactId) {
        viewModel.getContactById(contactId)
    }
    Text(contact.toString())
}