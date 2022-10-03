package com.example.mytesting

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                        perm.shouldShowRationale -> {
                        Text("Camera rationale true")
                            ShowRationale()
                        }
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

@Composable
fun ShowRationale() {

    val openDialog = remember { mutableStateOf(true)}

    if (openDialog.value)
        AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Permission Rationale")
        },
        text = {
            Text(
                "Audio Permission is required to record"
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { openDialog.value=false }
                ) {
                    Text("OK")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {  openDialog.value=false}
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}
