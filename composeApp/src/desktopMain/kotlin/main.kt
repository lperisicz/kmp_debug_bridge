import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    AppTheme {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DebugBridge",
        ) {
            App(modifier = Modifier.fillMaxSize())
        }
    }
}
