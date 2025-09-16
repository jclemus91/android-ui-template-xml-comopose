package com.jclemus.ui_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jclemus.ui_app.di.ServiceLocator
import com.jclemus.ui_app.feature.VmFactory
import com.jclemus.ui_app.feature.editor.NoteEditorScreen
import com.jclemus.ui_app.feature.list.NotesListScreen
import com.jclemus.ui_app.feature.NotesViewModel
import com.jclemus.ui_app.ui.theme.UiappTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = ServiceLocator.repository(this)
        setContent {
            UiappTheme {
                val navController = rememberNavController()
                val viewModel: NotesViewModel = viewModel(factory = VmFactory(repository))

                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        NotesListScreen(
                            viewModel,
                            onCreateNote = {
                                navController.navigate("edit")
                            },
                            onEditNote = { id ->
                                navController.navigate("edit?id=$id")
                            }
                        )
                    }

                    composable(
                        "edit?id={id}",
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.LongType; defaultValue = -1L

                            }
                        )
                    ) {
                        NoteEditorScreen(
                            viewModel,
                            noteId = it.arguments?.getLong("id")?.takeIf { value -> value != -1L },
                            onDone = { navController.popBackStack()},
                            onPersistUri = { uri ->
                                try {
                                    contentResolver.takePersistableUriPermission(
                                        uri.toUri(),
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