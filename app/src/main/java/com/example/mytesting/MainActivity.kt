package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mytesting.databinding.ActivityMainBinding
import com.example.mytesting.ui.theme.MyTestingTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var requestActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val fragManager = supportFragmentManager
        val view = binding.root
        setContentView(view)
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

        binding.testingView.apply {
            fragMgr= fragManager
            onClick = {
                bluetoothAdapter?.startDiscovery()
            }

        }

        /*setContent {
           MyTestingTheme {
               // A surface container using the 'background' color from the theme
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colors.background
               ) {
                   val deviceState = receiver.stateflow.collectAsState()
                   val list = mutableListOf<BluetoothDevice>()
                   Testing(fragManager,deviceState,list) {
                       bluetoothAdapter?.startDiscovery()
                       Log.d(TAG, "onCreate: onclick begin tracnction")
                   }
               }
           }
       }*/

    }
//d.androids if you register on create, unregister on  destroy
//    if you register on resume, unregister on pause to prevent registering multiple times
    override fun onPause() {
        super.onPause()
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

val TAG = "mytestingDebug"
@SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
@Composable
fun Testing(
    fragMgr : FragmentManager,
    onclick: () -> Unit
) {
    val lazystate = rememberLazyListState()
    val deviceList = remember {
        mutableListOf<BluetoothDevice>()
    }
    val device by receiver.stateflow.collectAsState()
    LaunchedEffect(key1 = device) {
        deviceList.apply {
            device?.let{
                add(it)
            }
        }
    }

    LazyColumn(state = lazystate) {
        item {
            Row(){
                Text("Bluetooth Devices",
                    modifier = Modifier
                        .weight(3f),
                    color = Color.Blue)
                Button(modifier=Modifier
                    .weight(1f),
                    onClick = { onclick() }) {
                    Text("Refresh")
                }
            }
            BeginFrag(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val x = placeable.width + 100
                        layout(placeable.width,placeable.height){
                        placeable.placeRelative(x,0)
                        }
                    }
            ) {
                val fragmentTransaction = fragMgr.beginTransaction()
                val newRecycleViewAdaptorFragment = RecycleViewAdaptorFragment.newInstance(deviceList)
                fragmentTransaction.replace(R.id.fragmentContainer, newRecycleViewAdaptorFragment)
                fragmentTransaction.commit()
            }
        }
        item {
            Row() {
                Text("Device",
                    modifier = Modifier
                        .weight(1f)
                        .background(Brush.linearGradient(listOf(Color.Green, Color.Yellow))))
                Text("Hardware",
                    modifier = Modifier
                        .weight(1f)
                        .background(Brush.linearGradient(listOf(Color.Green, Color.Yellow))))
            }
        }
//        TODO: items don't show as soon as the devicelist is updated in launcheffect
        items(deviceList.size ) { index ->
            Log.d(TAG, "Testing: ${deviceList.size} $index $deviceList")
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

class TestingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    lateinit var fragMgr: FragmentManager
    var onClick :() -> Unit ={}

    @Composable
    override fun Content() {
        MyTestingTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Testing(fragMgr,onClick)
            }
        }
    }

}
@Composable
fun BeginFrag(
    modifier: Modifier,
    click: () -> Unit,
) {
    Button(
        modifier= modifier,
        onClick = { click()}) {
        Text("Show Fragment")
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTestingTheme {
//        Testing("Android")
    }
}