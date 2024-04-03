package view.result

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Screen
import model.ScreenView
import service.loadResultStateList
import topAppBar

@Composable
fun loadResultListView(screenStack: SnapshotStateList<ScreenView>) {
    val resultStateList = loadResultStateList()

    MaterialTheme {
        Scaffold(
            topBar = { topAppBar(screenStack) },
            bottomBar = {}
        ) {
            Box(Modifier.fillMaxSize()) {
                val listState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier.fillMaxSize().height(56.dp),
                    state = listState
                ) {
                    items(resultStateList) {item ->
                        Row(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .clickable { screenStack += ScreenView(Screen.RESULT_INFO, item.id.toString()) }
                        ) {
                            Text(
                                text = AnnotatedString(item.name),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1F),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = listState)
                )
            }
        }
    }
}