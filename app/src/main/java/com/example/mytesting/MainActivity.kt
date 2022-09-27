package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            HomeScreen { name,age->
                navController.navigate("page1/$name/$age")
            }
        }
        composable(
            route ="page1"  + "/{name}/{age}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue= "page1 param1"
                },
                navArgument("age"){
                    type = NavType.IntType
                    defaultValue= -1
                }
            )){
            Page1Screen(
                name= it.arguments?.getString("name")!!,
                age=it.arguments?.getInt("age")!! ) {
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
    onNavigate: (String,Int) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
       Text("this is home screen",
       fontSize = 42.sp,
       )
        var name by remember {
            mutableStateOf("")
        }
        var age by remember {
            mutableStateOf("")
        }
       TextField(value = name,
           onValueChange = {
           name = it
       })
        TextField(value = age,
            onValueChange = {
           age = it
       },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Button(onClick = {onNavigate(name.ifEmpty { " " },(age.ifEmpty { "0" }).toInt())}) {
            Text(text = "take to page 1")
        }
    }
}

@Composable
fun Page1Screen(
    name: String,
    age: Int,
    onNavigate: () -> Unit
) {
        Column(
            modifier= Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("name is $name")
            Text("age is $age")
            Button(onClick = onNavigate) {
                Text(text = "page1 screen, from here to page 2",
                textAlign = TextAlign.Center)
            }
        }
}

@Composable
fun Page2Screen(
    onNavigate: () -> Unit
) {
    Button(onClick = onNavigate) {
        Text(text = "page2 screen, from here to page 3")
    }
}

@Composable
fun Page3Screen(
    onNavigate: () -> Unit
) {
    Button(onClick = onNavigate) {
        Text(text = "page3 screen, from here to home")
    }
}
