package logger

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.injectViewModel
import kotlinx.coroutines.launch

@Composable
internal fun LoggerScreen(
    viewModel: LoggerViewModel = injectViewModel(),
    modifier: Modifier = Modifier
) {
    val logState by viewModel
        .viewState<LoggerViewState.Logs>()
        .collectAsState(initial = LoggerViewState.Logs.initial)

    val coroutineScope = rememberCoroutineScope()
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(key1 = logState.output) {
        verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
    }

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                    shape = MaterialTheme.shapes.small
                )
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
                .padding(16.dp)
        ) {
            Text(
                text = logState.output,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Clip,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
        ) {
            IconButton(
                onClick = viewModel::clearOutput,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete logs",
                    tint = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
                        horizontalScrollState.animateScrollTo(0)
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = "Scroll to bottom",
                    tint = Color.White,
                )
            }
        }
    }
}
