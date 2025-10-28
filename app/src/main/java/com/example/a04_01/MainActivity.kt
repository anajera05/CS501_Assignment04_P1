package com.example.a04_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.a04_01.ui.theme._04_01Theme
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LifecycleEvent(val name: String, val timestamp: String, val color: Color)

class LifecycleViewModel: ViewModel() {
    private val _events = mutableStateListOf<LifecycleEvent>()
    val events: List<LifecycleEvent> = _events

    var showSnackbar by mutableStateOf(true)
    fun addEvent(name: String) {
        val time  = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val color = when (name) {
            "onCreate" -> Color.Blue
            "onStart" -> Color.Green
            "onResume" -> Color.Cyan
            "onPause" -> Color.Yellow
            "onStop" -> Color.DarkGray
            "onDestroy" -> Color.Red
            else -> Color.Gray
        }
        _events.add(LifecycleEvent(name, time, color))
    }
}


class MainActivity : ComponentActivity() {
    private val viewModel: LifecycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            _04_01Theme {
                val snackbarHostState = remember{ SnackbarHostState() }
                val scope = rememberCoroutineScope()

                DisposableEffect(Unit) {
                    val observer = object : DefaultLifecycleObserver {
                        override fun onCreate(owner: LifecycleOwner) {
                            viewModel.addEvent("onCreate")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onCreate")
                            }
                        }

                        override fun onStart(owner: LifecycleOwner) {
                            viewModel.addEvent("onStart")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onStart")
                            }
                        }

                        override fun onResume(owner: LifecycleOwner) {
                            viewModel.addEvent("onResume")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onResume")
                            }
                        }

                        override fun onPause(owner: LifecycleOwner) {
                            viewModel.addEvent("onPause")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onPause")
                            }
                        }

                        override fun onStop(owner: LifecycleOwner) {
                            viewModel.addEvent("onStop")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onStop")
                            }
                        }

                        override fun onDestroy(owner: LifecycleOwner) {
                            viewModel.addEvent("onDestroy")
                            if (viewModel.showSnackbar) scope.launch {
                                snackbarHostState.showSnackbar("Lifecycle: onDestroy")
                            }
                        }
                    }

                    lifecycle.addObserver(observer)
                    onDispose { lifecycle.removeObserver(observer) }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {SnackbarHost(snackbarHostState)}
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text("Show Snackbars")
                            Switch(
                                checked = viewModel.showSnackbar,
                                onCheckedChange = { viewModel.showSnackbar = it }
                            )
                        }
                        EventList(events = viewModel.events)

                    }
                }
            }
        }
    }
}

@Composable
fun EventList(events: List<LifecycleEvent>) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items(events) { event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(event.color.copy(alpha = .2f))
                    .padding(8.dp)
            ) {
                Text("${event.timestamp} - ${event.name}", color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _04_01Theme {

    }
}