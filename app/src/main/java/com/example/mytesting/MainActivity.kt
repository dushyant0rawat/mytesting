package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mytesting.ui.theme.MyTestingTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val permissionsState = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA
                        )
                    )
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED){
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    }
                    GetContentExample(permissionsState)
                }
            }
        }
    }


    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetContentExample(state: MultiplePermissionsState) {

    Column(
        modifier= Modifier
            .fillMaxSize()
    ){
        /*perm.permissionRequested is always false
        * rationale is
        * 1) false before first time time before don't ask dialogue
        * 2) true  when don't ask dialogue appears and permission is denied without dont ask again
        * 3) false when denied with don't ask again
        * permanentley denied condition could also be !perm.hasPermission && perm.shouldShowRationale*/
        state.permissions.forEach { perm->
            Text("permission: ${perm.permission} \n rationale: ${perm.shouldShowRationale}\n" +
                    "haspermission: ${perm.hasPermission} \n permissionrequested: ${perm.permissionRequested}")
            when(perm.permission){
                Manifest.permission.CAMERA -> {

                    when {
                        perm.hasPermission -> Text("Camera permission granted")
                        perm.permissionRequested -> Text("Camera permission requested before")
                        perm.shouldShowRationale -> Text("Camera rationale true")
                        else-> Text("Camera permission denied permanently")
                    }
                }
                Manifest.permission.RECORD_AUDIO ->{
                    when {
                        perm.hasPermission -> Text("audio permission granted")
                        perm.permissionRequested -> Text("record audio permission requested before")
                        perm.shouldShowRationale -> Text("Audio rationale true")
                        else-> Text("audio permission denied permanently")

                    }

                }
            }
        }
    }

}
