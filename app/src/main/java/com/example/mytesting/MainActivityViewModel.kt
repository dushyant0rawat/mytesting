package com.example.mytesting

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * simulate process death by pressing home key to app in background and terminating app in logcat
 * overflow menu and then bringing back app using recent activity button. previous ps shows dead and new process
 * id is created. In this case saveintance state is called and restoreinstance is subsequently called on restore
 * if app is not put in background using home button, terminate button kills the app but save state handle
 * is null
 */
class MainActivityViewModel(val num: Int, val str : String,private val state : SavedStateHandle) : ViewModel() {
    var mInt = 0
    init{
        Log.d(TAG, ": save state handle is ${state.get<Int>("test")}")
    }

    private var vmInt: Int =0
        set(value){
            field= value
            state.set("test",value)
        }

    fun changeVmInt() {
        vmInt = 2
        Log.d(TAG, "changeVmInt: $vmInt")
    }
    fun greet(){
        println("")
    }
}
