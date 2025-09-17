package com.jclemus.ui_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jclemus.ui_app.di.ServiceLocator
import com.jclemus.ui_app.feature.NotesViewModel
import com.jclemus.ui_app.feature.VMFactory
import com.jclemus.ui_app.feature.editor.NoteEditorScreen
import com.jclemus.ui_app.feature.list.NotesListScreen
import com.jclemus.ui_app.ui.theme.UiappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UiappTheme {
                val navController = rememberNavController()
                val viewModel: NotesViewModel = viewModel(factory = VMFactory(ServiceLocator.getRepository(this)))

                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        NotesListScreen(
                            viewModel = viewModel,
                            onCreate = {
                                navController.navigate("edit?id=-1")
                            },
                            onEdit = { id ->
                                navController.navigate("edit?id=$id")
                            }
                        )
                    }
                    composable(
                        route= "edit?id={id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.LongType
                            defaultValue = -1
                        })
                        ) { backStack: NavBackStackEntry ->
                        val id = backStack.arguments?.getLong("id") ?: -1
                        NoteEditorScreen(
                            viewModel = viewModel,
                            noteId = id,
                            onDone = {
                                navController.popBackStack()
                            },
                            onPersistUri = {
                                try {
                                    contentResolver.takePersistableUriPermission(
                                        it.toUri(),
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    )
                                } catch (_: Exception) {}
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UiappTheme {
        Greeting("Android")
    }
}