package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytesting.ui.theme.MyTestingTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GetContentExample()
                }
            }
        }
    }


    }

@Composable
fun GetContentExample() {
    val navController = rememberNavController()
    NavHost(
    navController = navController,
        startDestination = "home"
    ){
        composable(route ="home"){
            HomeScreen {
                navController.navigate("page1")
            }
        }
        composable(route ="page1"){
            Page1Screen {
                navController.navigate("page2")
            }
        }
        composable(route ="page2"){
            Page2Screen {
                navController.navigate("page3")
            }
        }
        composable(route ="page3"){
            Page3Screen{
                navController.navigate("home")
            }
        }
    }


}

@Composable
fun HomeScreen(
    onNavigateToFriends: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ){
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
       Text("this is home screen")
        Button(onClick = onNavigateToFriends) {
            Text(text = "take to page 1")
        }
    }
}

@Composable
fun Page1Screen(
    onNavigateToFriends: () -> Unit
) {
    Button(onClick = onNavigateToFriends) {
        Text(text = "page1 screen, from here to page 2")
    }
}

@Composable
fun Page2Screen(
    onNavigateToFriends: () -> Unit
) {
    Button(onClick = onNavigateToFriends) {
        Text(text = "page2 screen, from here to page 3")
    }
}

@Composable
fun Page3Screen(
    onNavigateToFriends: () -> Unit
) {
    Button(onClick = onNavigateToFriends) {
        Text(text = "page3 screen, from here to home")
    }
}
