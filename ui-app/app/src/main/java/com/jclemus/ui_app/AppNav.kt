package com.jclemus.ui_app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jclemus.ui_app.presentation.NotesEditorScreen
import com.jclemus.ui_app.presentation.NotesListScreen
import com.jclemus.ui_app.presentation.NotesViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            val viewModel: NotesViewModel = hiltViewModel()
            NotesListScreen(
                viewModel,
                onAdd = { navController.navigate("editor") },
                onEdit = { id -> navController.navigate("editor?noteId=$id")}
            )
        }

        composable(
            route="editor?noteId={noteId}",
            arguments = listOf(navArgument(
                name = "noteId") {
                type = NavType.LongType
                defaultValue = -1
            })
        ) {
                val viewModel: NotesViewModel = hiltViewModel()
                NotesEditorScreen(
                    viewModel,
                    noteId = it.arguments?.getLong("noteId") ?: -1,
                    onDone = {
                        navController.popBackStack()
                    }
                )
            }
    }
}