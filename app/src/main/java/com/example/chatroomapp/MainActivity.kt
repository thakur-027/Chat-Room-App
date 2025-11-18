package com.example.chatroomapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroomapp.screen.ChatRoomListScreen
import com.example.chatroomapp.screen.ChatScreen
import com.example.chatroomapp.screen.LoginScreen
import com.example.chatroomapp.screen.SignUpScreen
import com.example.chatroomapp.ui.theme.ChatRoomAppTheme
import com.example.chatroomapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            ChatRoomAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        // 1. Sign Up Screen
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }

        // 2. Login Screen
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignupScreen.route)
                },
                onSignInSuccess = {
                    // Navigate to Chat Rooms and remove Login from backstack
                    navController.navigate(Screen.ChatRoomsScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Chat Rooms List Screen
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen(
                onJoinClicked = { room ->
                    // Navigate to ChatScreen and pass the unique Room ID
                    navController.navigate("${Screen.ChatScreen.route}/${room.id}")
                }
            )
        }

        // 4. Chat Screen (Dynamic Route with Argument)
        composable("${Screen.ChatScreen.route}/{roomId}") { backStackEntry ->
            val roomId: String = backStackEntry.arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}