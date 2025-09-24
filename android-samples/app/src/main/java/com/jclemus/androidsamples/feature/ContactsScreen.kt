package com.jclemus.androidsamples.feature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jclemus.androidsamples.domain.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(viewModel: ContactViewModel, onContactClick: (Long) -> Unit) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts", style = MaterialTheme.typography.titleMedium) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {


            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search contacts") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Contacts", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.contacts, key = { it.id }) { contact ->

                        ContactRow(
                            contact = contact,
                            onContactClick = {
                                onContactClick.invoke(contact.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(contact: Contact, onContactClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onContactClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.name.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = contact.name
            )
            Text(text = contact.email)
            if (contact.company.isNotBlank()) {
                Text(text = contact.company)

            }

            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "View details"
            )

        }

    }
}