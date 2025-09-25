package com.jclemus.androidsamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jclemus.androidsamples.feature.SocialFeedScreen
import com.jclemus.androidsamples.ui.theme.UiappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UiappTheme {

                SocialFeedScreen()
            }
        }
    }
}
