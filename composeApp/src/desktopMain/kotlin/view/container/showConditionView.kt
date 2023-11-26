package view.container

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.Screen
import model.ScreenView
import model.view.CableState
import model.view.ContainerState
import service.calculateTwoDimensionalPacking
import service.hasDuplicatedNames
import service.loadContainerStateList
import topAppBar
import view.createFieldView
import view.isNonNegativeInteger

@Composable
fun showConditionView(screenStack: SnapshotStateList<ScreenView>) {
    val selectedContainerList = remember { mutableStateOf(emptyList<ContainerState>()) }
    val containerStateList = loadContainerStateList()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val cableStateList = remember { mutableStateOf(emptyList<CableState>())}
    var forceRefresh by remember { mutableStateOf(0) }
    var cableAddingCount = 1

    MaterialTheme {
        Scaffold(
            topBar = { topAppBar(screenStack) },
            bottomBar = {
                BottomAppBar {
                    Button(
                        onClick = {
                            cableStateList.value = (cableStateList.value + CableState(
                                id = cableAddingCount,
                                name = cableAddingCount.toString()
                            )).sortedByDescending(CableState::id)
                            cableAddingCount++
                        }
                    ) {
                        Text("Add Cable")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                val cableList = cableStateList.value.map(CableState::toCable)
                                if (hasDuplicatedNames(cableList)) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Duplicated Cable Name",
                                        actionLabel = "Go Back"
                                    )
                                } else {
                                    calculateTwoDimensionalPacking(
                                        selectedContainerList.value.map(ContainerState::toContainer),
                                        cableStateList.value.map(CableState::toCable)
                                    )
                                    screenStack.removeLast()
                                    screenStack.add(ScreenView(Screen.CONTAINER_LIST))
                                }
                            }
                        }
                    ) {
                        Text("Execute Packing Algorithm")
                    }
                }
            },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                Column(Modifier.fillMaxSize()) {
                    val lazyContainerListState = rememberLazyListState()
                    val containerList = remember { containerStateList }


                    Text(
                        text = "Container List",
                        modifier = Modifier.fillMaxSize().weight(1f).border(1.dp, Color.Black).background(Color.Gray)
                    )

                    Row(
                        modifier = Modifier.fillMaxSize().weight(1f).border(1.dp, Color.Black).background(Color.Gray)
                    ) {
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "select", 10f)
                        createFieldView(Modifier.weight(6f).border(1.dp, Color.Black), "name", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "count", 10f)
                    }

                    Box(Modifier.fillMaxSize().weight(8f).border(1.dp, Color.Black)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = lazyContainerListState
                        ) {
                            items(containerList) { item ->
                                val isSelected = remember { mutableStateOf(false) }

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(1.dp, Color.Black)
                                ) {
                                    Box(modifier = Modifier.weight(1f).height(56.dp).border(1.dp, Color.Black)) {
                                        Checkbox(
                                            checked = isSelected.value,
                                            onCheckedChange = { isSelected.value = it },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(modifier = Modifier.height(56.dp).weight(6f).border(1.dp, Color.Black)) {
                                        Text(
                                            text = AnnotatedString(item.name),
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.fillMaxSize(),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Box(modifier = Modifier.weight(1f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.count.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 5)) {
                                                    item.count = value.toIntOrNull() ?: 1
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize(),
                                            enabled = isSelected.value
                                        )
                                    }
                                }

                                LaunchedEffect(isSelected, item.count) {
                                    if (isSelected.value) {
                                        selectedContainerList.value = selectedContainerList.value.filter { it.name != item.name } + item
                                    } else {
                                        selectedContainerList.value = selectedContainerList.value.filter { it.name != item.name }
                                    }
                                }
                            }
                        }

                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                            adapter = rememberScrollbarAdapter(scrollState = lazyContainerListState)
                        )
                    }


                    val lazyCableListState = rememberLazyListState()


                    Text(
                        text = "Cable List",
                        modifier = Modifier.fillMaxSize().weight(1f).border(1.dp, Color.Black).background(Color.Gray)
                    )


                    Row(
                        modifier = Modifier.fillMaxSize().weight(1f).border(1.dp, Color.Black).background(Color.Gray)
                    ) {
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "name", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "width(mm)", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "length(mm)", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "height(mm)", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "weight(mm)", 10f)
                        createFieldView(Modifier.weight(1f).border(1.dp, Color.Black), "count", 10f)
                    }

                    Box(Modifier.fillMaxSize().weight(8f).border(1.dp, Color.Black)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = lazyCableListState
                        ) {
                            items(cableStateList.value) { item ->
                                Row(
                                    Modifier.border(1.dp, Color.Black)
                                ) {

                                    Box(Modifier.weight(2f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.name,
                                            onValueChange = { value ->
                                                if (value.isNotBlank() && (value.length <= 30)) {
                                                    item.name = value
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(Modifier.weight(2f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.width.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 9)) {
                                                    item.width = value.toIntOrNull()?: 0
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(Modifier.weight(2f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.length.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 9)) {
                                                    item.length = value.toIntOrNull()?: 0
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(Modifier.weight(2f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.height.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 9)) {
                                                    item.height = value.toIntOrNull()?: 0
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(Modifier.weight(2f).border(1.dp, Color.Black)) {
                                        OutlinedTextField(
                                            value = item.weight.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 9)) {
                                                    item.weight = value.toIntOrNull()?: 0
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(Modifier.weight(2f)) {
                                        OutlinedTextField(
                                            value = item.count.toString(),
                                            onValueChange = { value ->
                                                if (isNonNegativeInteger(value) && (value.length < 5)) {
                                                    item.count = value.toIntOrNull()?: 0
                                                } else {
                                                    forceRefresh++
                                                }
                                            },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                LaunchedEffect(item) {
                                    cableStateList.value = (cableStateList.value.filter { it.id != item.id } + item).sortedByDescending(CableState::id)
                                }
                            }
                        }

                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                            adapter = rememberScrollbarAdapter(scrollState = lazyCableListState)
                        )
                    }
                }
            }
        }
    }
}