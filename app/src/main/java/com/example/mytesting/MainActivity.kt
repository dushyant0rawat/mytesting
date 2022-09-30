package com.example.mytesting

import android.os.Bundle
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
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.mytesting.ui.theme.MyTestingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val gInt = 0
fun greet(){}
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
        /**
         * comment kotlin reflection and use java reflection because using kotlin reflection
         * requires dependency
         */
        val kclz = this::class
        val clz = kclz.java

/*        Log.d(TAG, "onCreate: simple ${kclz.simpleName} " +
                "parameters ${kclz.typeParameters} ${kclz.java}")
        for( constructor in kclz.constructors){
            Log.d(TAG, "onCreate:  constructor is $constructor")
        }*/
        for( constructor in clz.constructors){
            Log.d(TAG, "onCreate:  constructor is $constructor")
        }
        for( constructor in clz.declaredConstructors){
            Log.d(TAG, "onCreate:  declared constructor is $constructor")
        }
        Log.d(TAG, "onCreate: canonical name ${clz.canonicalName} " )
        val kprop0 = ::gInt
        val kprop1 = MainActivityViewModel::num
        MainActivityViewModel::mInt.set(viewmodel,2)
        val kfun0 = ::greet
        val kfun1 = MainActivityViewModel::greet
        Log.d(TAG, "onCreate: $kprop0 $kprop1 $kfun0 $kfun1")
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
    Log.d(TAG, "Testing: obj.nStr")
    Log.d(TAG, "Testing: obj.demo()")

    var par = parent()
    var par1 = parent1()
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
   /* class cast exception, can't cast downcast
   par1 = par as parent1*/
    par1.parent1Fun()
    val test = classImpl()

    Log.d(TAG, "test interface is ${test.test} ${test.hello}" +
            " 1: ${test.ext1()}" +
            " 2: ${test.ext2()}  " +
            "3 : ${test.ext3()}  " +
            "T: $test " +
            "s: ${test.ext1}")
    val p : parentInterface = test
    Log.d(TAG, "Testing:  parrent interface is ${p.s}  ")
    lateinit var prefs: Prefs
    prefs= Prefs(0f,0,0,0,MutableList(5){"1"})
    prefs.history.forEachIndexed { index, s ->
        Log.d(TAG, "Testing: $index $s")}
    prefs = Prefs(0f,0,0,0,MutableList(6){"2"})
    prefs.history.forEachIndexed { index, s ->
        Log.d(TAG, "Testing: $index $s")}
    val list1 = List(5){0}
    customPrint(list1)
    val list2 = List(5){0f}
    customPrint(list2)
    val list3 = List(5){0.0}
    customPrint(list3)
}

inline fun <reified T>  customPrint(list : List<T>){
    Log.d(TAG, "customPrint: list of type is ${T::class.java}" +
            "list of type is $list")

}
open class parent {
    open fun parentFun(){
        Log.d(TAG, "parentFun: parent")
    }
}

class parent1 : parent() {
    override fun parentFun() {
        Log.d(TAG, "parentFun: parent1 ")
    }
    fun parent1Fun(){
        Log.d(TAG, "parent1Fun: parent1")
    }
}
class parent2 : parent(){
    override fun parentFun() {
        Log.d(TAG, "parentFun: parent2 ")
    }
    fun parent2Fun(){
        Log.d(TAG, "parent1Fun: parent2")
    }
}
class parent3 : parent(){
    override fun parentFun() {
        Log.d(TAG, "parentFun: parent3 ")
    }
    fun parent3Fun(){
        Log.d(TAG, "parent1Fun: parent3")
    }
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
    Log.d(TAG, "returnNothing: this is nothing ${T::class.simpleName}" +
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
    Log.d(TAG, "print: ${value.javaClass}")
    when(value){
        is Integer -> Log.d(TAG, "print: this is integer")
        is Float -> Log.d(TAG, "print: this is Float")
        is String -> Log.d(TAG, "print: this is  String")
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