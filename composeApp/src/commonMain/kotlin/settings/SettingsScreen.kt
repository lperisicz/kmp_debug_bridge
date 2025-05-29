package settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.CustomOutlinedTextField
import components.injectViewModel

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = injectViewModel(),
    modifier: Modifier = Modifier,
) {
    val workspace by viewModel
        .viewState<SettingsViewState.Input>()
        .collectAsState(SettingsViewState.Input.initial)

    Column(modifier = modifier) {
        CustomOutlinedTextField(
            value = workspace.packageName,
            singleLine = true,
            onValueChange = viewModel::setPackageName,
            label = { Text("App package name") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            CustomOutlinedTextField(
                value = workspace.workspace,
                singleLine = true,
                onValueChange = viewModel::setWorkspace,
                label = { Text("Workspace Directory") },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = viewModel::chooseWorkspace,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = "Refresh available devices",
                    tint = Color.White,
                )
            }
        }
    }
}
