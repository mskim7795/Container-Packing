package view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Screen
import model.ScreenView
import topAppBar

@Composable
fun loadIndexView(screenStack: SnapshotStateList<ScreenView>) {
    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            topAppBar(screenStack)

            Button(
                onClick = { screenStack += ScreenView(Screen.CONTAINER_LIST) },
                modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color.Black)
            ) {
                Text(Screen.CONTAINER_LIST.id)
            }

            Button(
                onClick = { screenStack += ScreenView(Screen.NEW_CONDITION) },
                modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color.Black)
            ) {
                Text(Screen.NEW_CONDITION.id)
            }

            Button(
                onClick = { screenStack += ScreenView(Screen.RESULT_LIST) },
                modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color.Black)
            ) {
                Text(Screen.RESULT_LIST.id)
            }
        }
    }
}
