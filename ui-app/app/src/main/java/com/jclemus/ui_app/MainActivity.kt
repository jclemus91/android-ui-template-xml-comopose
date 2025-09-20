package com.jclemus.ui_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jclemus.ui_app.feature.explore.ExploreScreen
import com.jclemus.ui_app.feature.ExploreViewModel
import com.jclemus.ui_app.feature.detail.DetailScreen
import com.jclemus.ui_app.ui.theme.UiappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UiappTheme {

                NavApp()
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "explore"
    ) {
        composable("explore") {
            val viewModel: ExploreViewModel = viewModel()
            ExploreScreen(
                viewModel = viewModel
            ) { id, title ->
                navController.navigate("detail/$id/$title")
            }
        }

        composable("detail/{id}/{title}") {
            val viewModel: ExploreViewModel = viewModel()
            val id = it.arguments?.getString("id")?.toLong() ?: -1
            val title = it.arguments?.getString("title").orEmpty()

            DetailScreen(viewModel, id, title)
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