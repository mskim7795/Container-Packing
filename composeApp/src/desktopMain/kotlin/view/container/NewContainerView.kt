package view.container

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.ScreenView
import model.view.ContainerState
import service.saveContainer
import topAppBar
import view.createFieldView
import view.createUnitView
import view.isNonNegativeInteger
import java.lang.Exception

@Composable
fun saveContainerView(screenStack: SnapshotStateList<ScreenView>) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val containerState = remember { ContainerState() }
    val errorMap = remember { mutableStateMapOf(
        "width" to false,
        "length" to false,
        "height" to false,
        "weight" to false,
        "cost" to false
    ) }

    MaterialTheme {
        Scaffold(
            topBar = { topAppBar(screenStack) },
            bottomBar = {
                BottomAppBar {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    if(saveContainer(containerState.toContainer())) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Save succeeded",
                                            actionLabel = "OK"
                                        )
                                        screenStack.removeLast()
                                    } else {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Duplicated Name",
                                            actionLabel = "Go Back",
                                        )
                                    }
                                } catch (e: Exception) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Unexpected error",
                                        actionLabel = "Go Back",
                                    )
                                }
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }
            },
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                ) {
                    Row(
                        Modifier.weight(1f).border(1.dp, Color.Black)
                    ) {
                        val text = "name"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 10f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.name,
                                onValueChange = {
                                    errorMap[text] = it.isBlank() || it.length > 30
                                    if (errorMap[text] != true) {
                                        containerState.name = it
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        Box(Modifier.weight(0.2f)) {
                            Text(
                                text = "",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(1.dp, Color.Black)
                    ) {
                        val text = "width"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 10f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.width.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.width = value.toIntOrNull() ?: 0
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        createUnitView(Modifier.weight(0.2f), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(1.dp, Color.Black)
                    ) {
                        val text = "length"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 10f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.height.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 4)
                                    if (errorMap[text] != true) {
                                        containerState.height = value.toIntOrNull() ?: 0
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        createUnitView(Modifier.weight(0.2f), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(1.dp, Color.Black)
                    ) {
                        val text = "weight"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 10f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.weight.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.weight = value.toIntOrNull() ?: 0
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(1.dp, Color.Black)
                    ) {
                        val text = "cost"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 10f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.cost.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.cost = value.toIntOrNull() ?: 0
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f), "mm")
                    }
                }
            }
        }
    }
}
