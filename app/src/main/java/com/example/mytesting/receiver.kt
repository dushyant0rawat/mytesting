package com.example.mytesting

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object receiver : BroadcastReceiver() {

    private val _mutableStateFlow= MutableStateFlow<BluetoothDevice?>(null)
    val stateflow= _mutableStateFlow.asStateFlow()
    var isRegistered = false
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val action: String? = intent.action
        when (action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                _mutableStateFlow.value = device
                val deviceName = device?.name
                val deviceHardwareAddress = device?.address // MAC address
                Log.d(TAG, "onReceive: $deviceName $deviceHardwareAddress")
            }
        }
    }
}