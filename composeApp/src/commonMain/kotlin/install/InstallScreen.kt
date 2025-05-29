package install

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilePresent
import androidx.compose.material.icons.outlined.InstallMobile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.CustomOutlinedTextField
import components.injectViewModel
import logger.LoggerScreen

@Composable
internal fun InstallScreen(
    viewModel: InstallViewModel = injectViewModel(),
    modifier: Modifier = Modifier
) {
    val fileState by viewModel
        .viewState<InstallViewState.FileState>()
        .collectAsState(InstallViewState.FileState.initial)

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            CustomOutlinedTextField(
                value = fileState.path ?: "",
                onValueChange = viewModel::setPath,
                singleLine = true,
                label = { Text("Install file path") },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = viewModel::choosePath,
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilePresent,
                    contentDescription = "Refresh available devices",
                    tint = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = viewModel::install,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent
            ),
            enabled = fileState.actionEnabled,
        ) {
            Icon(
                imageVector = Icons.Outlined.InstallMobile,
                contentDescription = "Install",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Install file")
        }
        Spacer(modifier = Modifier.height(128.dp))
        LoggerScreen(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}
