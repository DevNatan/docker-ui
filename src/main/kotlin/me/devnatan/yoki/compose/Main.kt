package me.devnatan.yoki.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import me.devnatan.yoki.compose.screens.ScreenA
import me.devnatan.yoki.compose.screens.ScreenB

@Composable
@Preview
private fun Preview() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Content(isLoading = true, errorMessage = null, containers = emptyList(), onRefresh = {})
        Content(isLoading = false, errorMessage = null, containers = emptyList(), onRefresh = {})
        Content(isLoading = false, errorMessage = "Deu ruim", containers = emptyList(), onRefresh = {})
        Content(isLoading = false, errorMessage = null, containers = listOf(
            ContainerModel("Piroca de ofice"),
            ContainerModel("Fake natty"),
            ContainerModel("Ainda n me chame de bb")
        ), onRefresh = {})

    }
}

@Composable
fun App(isLoading: Boolean, errorMessage: String?, containers: List<ContainerModel>, onRefresh: () -> Unit) {
    MaterialTheme {
        Content(isLoading, errorMessage, containers, onRefresh)
    }
}

@Composable
private fun Content(
    isLoading: Boolean,
    errorMessage: String?,
    containers: List<ContainerModel>,
    onRefresh: () -> Unit
) {
    Row {
        Column {
            when {
                isLoading -> Text("Carregando...")
                errorMessage != null -> Text("Deu ruim: $errorMessage")
                else -> ContainerList(containers)
            }
        }
        Button(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}

@Composable
private fun ContainerList(containers: List<ContainerModel>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (container in containers) {
            Container(name = container.name)
        }
    }
}

data class ContainerModel(val name: String)

@Composable
private fun Container(name: String) {
    Column {
        Text(text = name)
    }
}

//fun main() {
//    val yoki = Yoki.createWithHttpDefaults()
//
//    application {
//        Window(onCloseRequest = ::exitApplication) {
//            var isLoading by remember { mutableStateOf(true) }
//            var isRefreshing by remember { mutableStateOf(false) }
//            var errorMessage by remember { mutableStateOf<String?>(null) }
//            var containerList = remember { mutableStateListOf<ContainerModel>() }
//
//            LaunchedEffect(isRefreshing) {
//                if (isLoading || isRefreshing) {
//                    containerList = runCatching {
//                        yoki.containers.list()
//                    }.onFailure { error -> errorMessage = error.message }
//                        .getOrElse { emptyList() }
//                        .map { container -> ContainerModel(name = container.name) }
//                        .toMutableStateList()
//                }
//                isLoading = false
//                isRefreshing = false
//            }
//
//            App(isLoading, errorMessage, containerList, onRefresh = { isRefreshing = true })
//        }
//    }
//}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        Navigator(listOf(ScreenB(), ScreenA()))
    }
}