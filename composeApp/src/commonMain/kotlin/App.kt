import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import di.concurrencyModule
import di.recordingModule
import home.HomeScreen
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.compose.KoinApplication

@Composable
fun App(modifier: Modifier = Modifier) {
    val coroutineScope = rememberAppCoroutineScope()

    KoinApplication(
        application = {
            modules(
                concurrencyModule(coroutineScope),
                recordingModule,
            )
        }
    ) {
        HomeScreen(modifier = modifier.background(Color(0xFF1E2022)))
    }
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFFFFFFFF), // A deep purple
            primaryVariant = Color(0xFFFFFFFF), // A more intense purple
            secondary = Color(0xFFFFFFFF), // A teal color
            secondaryVariant = Color(0xFFFFFFFF), // A darker teal
            background = Color(0xFF121212), // Dark grey background
            surface = Color(0xFF121212), // Dark grey surface
            error = Color(0xFFCF6679), // A soft red for errors
            onPrimary = Color.White, // White text/icons on primary
            onSecondary = Color.White, // White text/icons on secondary
            onBackground = Color.White, // White text/icons on background
            onSurface = Color.White, // White text/icons on surface
            onError = Color.White // Black text/icons on error
        ),
        content = content,
    )
}

@Composable
private fun rememberAppCoroutineScope(): CoroutineScope =
    rememberCoroutineScope {
        Dispatchers.Default +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    println("Exception in AppScope [$coroutineContext]: $throwable")
                }
    }
