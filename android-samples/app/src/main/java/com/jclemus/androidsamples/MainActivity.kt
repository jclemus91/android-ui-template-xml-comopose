package com.jclemus.androidsamples

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
import com.jclemus.androidsamples.ui.data.AirbnbPropertySearchEngine
import com.jclemus.androidsamples.ui.theme.UiappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UiappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val engine = AirbnbPropertySearchEngine()
                    val resultBruteForce = engine.findNearbyPropertiesBruteForce("prop1", 5)

                    val result = StringBuilder()
                    resultBruteForce.forEach { key, value ->
                        result.append("Level $key\n${value.joinToString { it.name }}\n\n")
                    }


                    val resultOptimized = engine.findNearbyPropertiesOptimized("prop1", 3)
                    result.clear()
                    resultOptimized.forEach { (first, second) ->
                        result.append("Level $second\n${first.name}\n\n")

                    }

                    val resultReal = engine.findSimilarPropertiesInNeighborhood(
                        engine.properties["prop5"]!!, IntRange(90 ,320), 4.65
                    )
                    result.clear()
                    resultReal.forEach { property ->
                        result.append(property.name)
                    }
                    Greeting(
                        name = result.toString(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
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