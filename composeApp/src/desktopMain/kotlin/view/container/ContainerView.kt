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
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.ScreenView
import service.deleteContainer
import service.loadContainerState
import service.updateContainer
import topAppBar
import view.createFieldView
import view.createUnitView
import view.isNonNegativeInteger
import java.lang.Exception

@Composable
fun loadContainerInfoView(screenStack: SnapshotStateList<ScreenView>) {
    val containerName = screenStack.last().itemName
    val containerState = remember {
        loadContainerState(containerName)
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val errorMap = remember { mutableStateMapOf(
        "width" to false,
        "length" to false,
        "height" to false,
        "weight" to false,
        "cost" to false
    ) }
    var canEdit by remember { mutableStateOf(false) }

    MaterialTheme {
        Scaffold(
            topBar = { topAppBar(screenStack) },
            bottomBar = {
                BottomAppBar {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    if(updateContainer(containerState.toContainer())) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Update succeeded",
                                            actionLabel = "OK"
                                        )
                                        screenStack.removeLast()
                                    } else {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Update failed",
                                            actionLabel = "Ok",
                                        )
                                    }
                                } catch (e: Exception) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Unexpected error",
                                        actionLabel = "Ok",
                                    )
                                }
                            }
                                  },
                    ) {
                        Text("Update")
                    }
                    Text("Edit:")
                    Switch(
                        checked = canEdit,
                        onCheckedChange = { canEdit = !canEdit }
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    if(deleteContainer(containerState.toContainer())) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Delete succeeded",
                                            actionLabel = "OK",

                                        )
                                        screenStack.removeLast()
                                    } else {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Delete failed",
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
                        }
                    ) {
                        Text("Delete")
                    }
                }
            },
            scaffoldState = scaffoldState,
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
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "name"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.name,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxSize()
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
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "width"
                        createFieldView(Modifier.weight(1f).align(Alignment.CenterVertically), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.width.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.width = value.toIntOrNull() ?: 0
                                    }
                                },
                                readOnly = !canEdit,
                                singleLine = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f).align(Alignment.Bottom), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "length"
                        createFieldView(Modifier.weight(1f), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.length.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 4)
                                    if (errorMap[text] != true) {
                                        containerState.length = value.toIntOrNull() ?: 0
                                    }
                                },
                                readOnly = !canEdit,
                                singleLine = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f).align(Alignment.Bottom), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "height"
                        createFieldView(Modifier.weight(1f), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.height.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 4)
                                    if (errorMap[text] != true) {
                                        containerState.height = value.toIntOrNull() ?: 0
                                    }
                                },
                                readOnly = !canEdit,
                                singleLine = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f).align(Alignment.Bottom), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "weight"
                        createFieldView(Modifier.weight(1f), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.weight.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.weight = value.toIntOrNull() ?: 0
                                    }
                                },
                                readOnly = !canEdit,
                                singleLine = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f).align(Alignment.Bottom), "mm")
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        Modifier.weight(1f).border(2.dp, Color.Black)
                    ) {
                        val text = "cost"
                        createFieldView(Modifier.weight(1f), text, 25f)

                        Box(Modifier.weight(2f)) {
                            OutlinedTextField(
                                value = containerState.cost.toString(),
                                onValueChange = {value ->
                                    errorMap[text] = !isNonNegativeInteger(value) || (value.length > 5)
                                    if (errorMap[text] != true) {
                                        containerState.cost = value.toIntOrNull() ?: 0
                                    }
                                },
                                readOnly = !canEdit,
                                singleLine = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        createUnitView(Modifier.weight(0.2f).align(Alignment.Bottom), "mm")
                    }
                }
            }
        }
    }
}
