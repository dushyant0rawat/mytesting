package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mytesting.ui.theme.MyTestingTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var requestActivityLauncher: ActivityResultLauncher<Intent>
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter : BluetoothAdapter? = bluetoothManager.adapter
        val pairedDevices: Set<BluetoothDevice>?=  bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }

        requestActivityLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()){result->
                if(result.resultCode == Activity.RESULT_OK){
                    Log.d(TAG, "onCreate: bluetooth enabled")
                }else{
                    Log.d(TAG, "onCreate: bluetooth enabled failed")
                }
            }
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestActivityLauncher.launch(enableBtIntent)

        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
       /*can't unregister in repeatonlifecycle because it is suspend function on State.STARTED
        state.created
        unregister is in onStop override
        */

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)
                receiver.isRegistered=true

            }

        }

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.forEach{
                    val permission = it.key
                    val isGranted = it.value
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Log.d(TAG,"callback permissions granted $permission")
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Log.d(TAG,"callback permissions NOT granted $permission")

                    }
                }
            }
        requestPermissionLauncher.launch(permissions)

        setContent {
            MyTestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Testing{
                        bluetoothAdapter?.startDiscovery()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (receiver.isRegistered) {
            receiver.isRegistered=false
            unregisterReceiver(receiver)
        }
    }
}

fun callback(result: ActivityResult) {
    if(result.resultCode == Activity.RESULT_OK){
        Log.d(TAG, "onCreate: bluetooth enabled")
    }else{
        Log.d(TAG, "onCreate: bluetooth enabled failed")
    }
}
// Create a BroadcastReceiver for ACTION_FOUND.

val TAG = "mytestingDebug"
@SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
@Composable
fun Testing(
    onclick: () -> Unit
) {
    val lazystate = rememberLazyListState()
    val deviceList = remember {
        mutableListOf<BluetoothDevice?>()
    }
    val device = receiver.stateflow.collectAsState().value
    LaunchedEffect(key1 = device) {
        deviceList.apply {
            if(device!=null){
                add(device)
            }
        }
    }

    LazyColumn(state = lazystate) {
        item {
            Row(){
                Text("Bluetooth Devices",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    color = Color.Blue)
                Button(modifier=Modifier
                    .weight(1f),
                    onClick = { onclick() }) {
                    Text("Refresh")
                }
            }
        }
        item {
            Row() {
                Text("Device",
                    modifier = Modifier
                        .weight(1f)
                        .background(Brush.linearGradient(listOf(Color.Green,Color.Yellow))))
                Text("Hardware",
                    modifier = Modifier
                        .weight(1f)
                        .background(Brush.linearGradient(listOf(Color.Green,Color.Yellow))))
            }
        }
        items(deviceList.size) { index ->
            Row() {
                Text("${deviceList[index]?.name}",
                    modifier = Modifier
                        .weight(1f))
                Text("${deviceList[index]?.address}",
                    modifier = Modifier
                        .weight(1f))
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTestingTheme {
//        Testing("Android")
    }
}