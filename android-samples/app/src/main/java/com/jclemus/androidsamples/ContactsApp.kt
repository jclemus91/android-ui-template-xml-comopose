package com.jclemus.androidsamples

import android.provider.ContactsContract
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jclemus.androidsamples.feature.ContactDetailScreen
import com.jclemus.androidsamples.feature.ContactViewModel
import com.jclemus.androidsamples.feature.ContactsScreen

object Destinations {
    const val CONTACTS = "contacts"
    const val DETAIL = "detail"
}

@Composable
fun ContactsApp() {
    val navController = rememberNavController()

    val viewModel: ContactViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = Destinations.CONTACTS
    ) {
        composable(Destinations.CONTACTS) {
            ContactsScreen(
                viewModel = viewModel,
                onContactClick = { contactId ->
                    navController.navigate("${Destinations.DETAIL}/$contactId")

                }
            )
        }

        composable(
            route = "${Destinations.DETAIL}/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
        ) { backStack ->
            val contactId = backStack.arguments?.getLong("contactId") ?: -1L
            ContactDetailScreen(
                contactId = contactId,
                viewModel  = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}



