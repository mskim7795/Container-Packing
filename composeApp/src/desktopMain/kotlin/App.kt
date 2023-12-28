import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import model.Screen
import model.ScreenView
import util.initializeRootFolder
import view.container.loadContainerListView
import view.container.createNewConditionView
import view.loadIndexView
import view.container.loadContainerInfoView
import view.container.saveContainerView
import view.result.loadResultInfoView
import view.result.loadResultListView

@Composable
fun app() {
    val screenStack = remember { mutableStateListOf(ScreenView(Screen.INDEX)) }
    initializeRootFolder()
    when (screenStack.last().screen) {
        Screen.INDEX -> loadIndexView(screenStack)
        Screen.CONTAINER_LIST -> loadContainerListView(screenStack)
        Screen.CONTAINER_INFO -> loadContainerInfoView(screenStack)
        Screen.NEW_CONTAINER -> saveContainerView(screenStack)
        Screen.NEW_CONDITION -> createNewConditionView(screenStack)
        Screen.RESULT_LIST -> loadResultListView(screenStack)
        Screen.RESULT_INFO -> loadResultInfoView(screenStack)
    }
}

@Composable
private fun goBack(screenStack: SnapshotStateList<ScreenView>) {
    IconButton(onClick = { screenStack.removeLast() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
    }
}

@Composable
private fun goHome(screenStack: SnapshotStateList<ScreenView>) {
    IconButton(onClick = {
        screenStack.clear()
        screenStack.add(ScreenView(Screen.INDEX))
    }) {
        Icon(Icons.Filled.Home, contentDescription = "Go Home")
    }
}

@Composable
fun topAppBar(screenStack: SnapshotStateList<ScreenView>) {
    val currentScreen = screenStack.last().screen
    TopAppBar(
        title = { Text(currentScreen.id) },
        navigationIcon = {
            if (currentScreen != Screen.INDEX) {
                goBack(screenStack)
                goHome(screenStack)
            }
        }
    )
}
