import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Container Packing",
        state = rememberWindowState(
            position = WindowPosition(alignment = Alignment.Center),
            width = 1280.dp,
            height = 720.dp
        ),
    ) {

        app()
    }
}
