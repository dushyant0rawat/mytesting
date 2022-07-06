package com.example.mytesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mytesting.ui.theme.MyTestingTheme

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
                    Testing("Android")
                }
            }
        }
    }
}

val TAG = "mytestingDebug"
@Composable
fun Testing(name: String) {

    val test = classImpl()

    println("test interface is ${test.test} ${test.hello}")
    val p : parentInterface = test
    println(" parrent interface is ${p.s}  ")
    lateinit var prefs: Prefs
    prefs= Prefs(0f,0,0,0,MutableList(5){"1"})
    prefs.history.forEachIndexed { index, s ->
        println("$index $s")}
    prefs = Prefs(0f,0,0,0,MutableList(6){"2"})
    prefs.history.forEachIndexed { index, s ->
        println("$index $s")}
}

data class Prefs (
    val level: Float,
    val rows: Int,
    val cols : Int,
    val historyLast: Int,
    val history: MutableList<String>
)

interface parentInterface {
    val s: String
    get() = "parent"
}
interface MyInterface : parentInterface {

    val test: Int

    fun foo() : String

    val hello : String
        get() = "Hello there, pal!"
}

fun classImpl() : MyInterface{
    return object : MyInterface {
        override val test: Int
            get() = 5

        override fun foo(): String {
            return "lol"
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTestingTheme {
        Testing("Android")
    }
}