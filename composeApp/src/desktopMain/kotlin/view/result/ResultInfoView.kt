package view.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import model.ScreenView
import model.view.DetailedContainerState
import model.view.SimpleCableState
import service.deleteResult
import service.loadResultState
import service.updateResultName
import topAppBar

@Composable
fun loadResultInfoView(screenStack: SnapshotStateList<ScreenView>) {
    val resultState = remember { loadResultState(screenStack.last().itemName) }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
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
                                    if(updateResultName(screenStack.last().itemName, resultState.name)) {
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
                                    if(deleteResult(resultState.name)) {
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
            val listState = rememberLazyListState()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = resultState.name,
                        onValueChange = {
                            if (!(it.isBlank() || it.length > 30)) {
                                resultState.name = it
                            }
                        },
                        readOnly = !canEdit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(7f).border(1.dp, Color.Black),
                    state = listState
                ) {
                    items(resultState.detailedContainerStateList) {detailedContainerState ->
                        var expanded by remember { mutableStateOf(false) }
                        val container = detailedContainerState.containerState
                        Row(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .clickable { expanded = !expanded }
                        ) {
                            Text(
                                text = AnnotatedString(container.name),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1F),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        AnimatedVisibility(visible = expanded) {
                            showSimpleCableList(detailedContainerState.simpleCableStateList)
                        }
                    }
                }

                Row(
                    modifier = Modifier.weight(1f)
                        .border(1.dp, Color.Black)
                ) {
                    Text(
                        text = "Remained Cable",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1F),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.weight(3f)
                        .border(1.dp, Color.Black)
                ) {
                    showSimpleCableList(resultState.remainedCableStateList)
                }
            }
        }
    }
}

@Composable
fun showSimpleCableList(simpleCableStateList: List<SimpleCableState>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(simpleCableStateList) { simpleCableState ->
            Row(
                modifier = Modifier
                    .border(1.dp, Color.Black)
            ) {
                Text(
                    text = "Cable Name:",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F).border(1.dp, Color.Black),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = AnnotatedString(simpleCableState.name),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F).border(1.dp, Color.Black),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Count:",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F).border(1.dp, Color.Black),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = AnnotatedString(simpleCableState.count.toString()),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1F).border(1.dp, Color.Black),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}