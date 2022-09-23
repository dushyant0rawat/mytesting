package com.example.mytesting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainActivityViewModel(num: Int, str : String,state : SavedStateHandle) : ViewModel() {
val num = num
}