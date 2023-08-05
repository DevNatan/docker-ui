package me.devnatan.yoki.compose.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import me.devnatan.yoki.compose.LocalYoki
import me.devnatan.yoki.compose.theme.AppTheme
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.system.SystemPingData
import me.devnatan.yoki.resource.container.list

private data class Container(val name: String, val status: String)

internal class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val yoki = LocalYoki.current
        val (pingData, setPingData) = remember { mutableStateOf<SystemPingData?>(null) }

        LaunchedEffect(Unit) {
            setPingData(yoki.system.ping())
        }

        val (isLoading, setLoading) = remember { mutableStateOf(true) }
        var containers by rememberSaveable(Unit) {
            mutableStateOf(emptyList<Container>())
        }

        var isRefreshing by remember { mutableStateOf(true) }
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                containers = yoki.containers
                    .list()
                    .map { summary ->
                        Container(
                            name = summary.id,
                            status = summary.status,
                        )
                    }
                    .toList()
            }
            setLoading(false)
            isRefreshing = false
        }

        Column(modifier = Modifier.padding(24.dp)) {
            when {
                pingData == null -> Pinging()
                isLoading -> Loading(pingData)
                else -> Containers(containers, isRefreshing, onRefresh = { isRefreshing = true })
            }
        }
    }


    @Composable
    private fun Containers(containers: List<Container>, isRefreshing: Boolean, onRefresh: () -> Unit) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onRefresh, enabled = !isRefreshing) {
                Text(text = "Refresh")
            }
            Text(text = "List of containers (${containers.size}):")
            for (container in containers) {
                ContainersItem(container)
            }
        }
    }

    @Composable
    private fun ContainersItem(container: Container) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = container.name)
            Text(text = "Status: ${container.status}")
        }
    }

    @Composable
    private fun Pinging() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pinging....")
        }
    }

    @Composable
    private fun Loading(ping: SystemPingData) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), ) {
                Text(text = "Loading containers...", textAlign = TextAlign.Center)
                Text(text = "API version: ${ping.apiVersion}", textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        HomeScreen().Content()
    }
}