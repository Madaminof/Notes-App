package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.todolist.composeUI.navigation.AppNavGraph
import com.example.todolist.composeUI.screen.settings.SettingsViewModel
import com.example.todolist.ui.theme.TodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkTheme by settingsViewModel.darkModeEnabled.collectAsState()

            TodoListTheme(darkTheme = isDarkTheme) {
                Surface {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }

    }
}
