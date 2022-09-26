package com.example.mytesting

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mytesting.ui.theme.MyTestingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* val mfactory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>) : T {
                return MainActivityViewModel(0,"",savedInstanceState)  as T
            }
        }   */
        class mfactory(
           owner: SavedStateRegistryOwner,
           defaultArgs: Bundle? = null)
           : AbstractSavedStateViewModelFactory(owner, defaultArgs){
           override fun <T : ViewModel?> create(
               key: String,
               modelClass: Class<T>,
               handle: SavedStateHandle,
           ): T {
               return MainActivityViewModel(0,"",handle) as T
           }
           /* override fun <T : ViewModel?> create(modelClass: Class<T>) : T {
            return MainActivityViewModel(0,"",savedInstanceState)  as T
        }*/
        }
        val viewmodel  : MainActivityViewModel by viewModels{
            mfactory(this,intent.extras)
        }
//        val viewmodel  : MainActivityViewModel by viewModels()

//        println("viewmodel num = ${viewmodel.num}")

        setContent {
            MyTestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Testing("Android")
                    viewmodel.changeVmInt()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("test",5)
        Log.d(TAG, "onSaveInstanceState: one parm")
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: ${savedInstanceState.get("test")}")
    }

}

val TAG = "mytestingDebug"
@Composable
fun Testing(name: String) {
    returnSomething<String>()
    returnSomething<Int>()
    returnSomething<Float>()
    print(4)
    print(4L)
    print(4f)
    print("string of any")
    // accessing data member of nested class
    println(OuterClass.NestedClass().nStr)

    // accessing member function of nested class
    println(OuterClass.NestedClass().demo())

    // creating object of the Nested class
    val obj = OuterClass.NestedClass()
    println(obj.nStr)
    println(obj.demo())

    val par = parent()
    val par1 = parent1()
    val par2 = parent2()
    val par3 = parent3()
    returnNothing("this is reified string")

    val repo1: repo by repoiml1()
    repo1.getallvalues()
    val typeofclass = fun(par: parent){
    when(par){
         is parent1 -> {
            Log.d(TAG," this is parent1")
        }
        is parent2 -> {
            Log.d(TAG," this is parent2")
        }
        is parent3 -> {
            Log.d(TAG," this is parent3")
        }

    }

    }
    Log.d(TAG," type of class par1 is ${typeofclass(par1)}")
    Log.d(TAG," type of class par2 is ${typeofclass(par2)}")
    Log.d(TAG," type of class par3 is ${typeofclass(par3)}")
    val test = classImpl()

    println("test interface is ${test.test} ${test.hello}" +
            " 1: ${test.ext1()}" +
            " 2: ${test.ext2()}  " +
            "3 : ${test.ext3()}  " +
            "T: $test " +
            "s: ${test.ext1}")
    val p : parentInterface = test
    println(" parrent interface is ${p.s}  ")
    lateinit var prefs: Prefs
    prefs= Prefs(0f,0,0,0,MutableList(5){"1"})
    prefs.history.forEachIndexed { index, s ->
        println("$index $s")}
    prefs = Prefs(0f,0,0,0,MutableList(6){"2"})
    prefs.history.forEachIndexed { index, s ->
        println("$index $s")}
    val list1 = List(5){0}
    customPrint(list1)
    val list2 = List(5){0f}
    customPrint(list2)
    val list3 = List(5){0.0}
    customPrint(list3)
}

inline fun <reified T>  customPrint(list : List<T>){
    println("list of type is ${T::class.java}")
    println("list of type is $list")

}
open class parent {}

class parent1 : parent() {}
class parent2 : parent(){}
class parent3 : parent(){}

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
    fun  ext1(): parentInterface {
        return this
    }
}


fun MyInterface.ext2(): parentInterface {
    return this
}
fun MyInterface.ext3(): MyInterface {
    return this
}

val parentInterface.ext1 : String
    get() = ""

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
// variances annotation are only allowed for type parameters of class and interfaces
//fun <out T> testgeneric() : T {
//
//}

inline fun <reified T>returnNothing(value: T) {
//    throw Throwable("This is return Nothing")
    println("this is nothing ${T::class.simpleName}" +
            "   =  ${T::class.java}")
 val list : Flow<Int> = flow{

 }
}

interface repo {
    fun getallvalues()
    fun getsomevalue()
}
// nothing is subtype of all classes
// and any is super of all classes
//TODO why Nothing? worked but not Nothing and Any
// nothing? is the reference because repoiml1 is called from a global function
// where there is this reference
class repoiml1 : ReadOnlyProperty<Nothing?, repo> {

    init{
        Log.d(TAG, ":I am implementation of repo " +
                "${repo::class.java}   ")
    }
    override operator fun getValue(thisref: Nothing?, property: KProperty<*>): repo {
        Log.d(TAG, "getValue: ${property.name}")
        return object : repo {
            override fun getallvalues(){

            }
            override fun getsomevalue(){

            }
        }
    }

}

class OuterClass {

    val oStr = "Outer Class"

    class NestedClass {
        val nStr = "Nested Class "
        fun demo() = "demo() function of nested class"
    }
}

class genericClass<T>(value :T ){

}
inline fun <reified T> returnSomething() {
    println(T::class.java)
}
fun print(value : Any){
    println("${value.javaClass}")
    when(value){
        is Integer -> println("this is integer")
        is Float -> println("this is Float")
        is String -> println("this is  String")
//        else  -> println("this is Any")
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTestingTheme {
        Testing("Android")
    }
}