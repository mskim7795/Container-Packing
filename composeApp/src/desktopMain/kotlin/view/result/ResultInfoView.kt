package view.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.ScreenView
import model.view.SimpleCableState
import service.deleteResult
import service.loadResultState
import service.updateResultName
import topAppBar
import java.util.UUID

@Composable
fun loadResultInfoView(screenStack: SnapshotStateList<ScreenView>) {
    val resultState = remember { loadResultState(UUID.fromString(screenStack.last().itemId)) }
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
                                    if(updateResultName(resultState.id, resultState.name)) {
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
                                    if(deleteResult(resultState.id)) {
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
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        readOnly = !canEdit,
                        modifier = Modifier.fillMaxSize().background(Color.Yellow)
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(7f).border(1.dp, Color.Black),
                    state = listState
                ) {
                    items(resultState.detailedContainerStateList) { detailedContainerState ->
                        var expanded by remember { mutableStateOf(false) }
                        val container = detailedContainerState.containerState
                        Row(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .clickable { expanded = !expanded }
                                .background(Color.Cyan)
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
                        .border(1.dp, Color.Black).background(Color.Gray)
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().height(200.dp).verticalScroll(scrollState),
    ) {
        simpleCableStateList.forEach { simpleCableState ->
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