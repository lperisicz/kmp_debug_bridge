package device

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import components.CustomOutlinedTextField
import components.injectViewModel
import qr.QRPairScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DevicePickerScreen(
    viewModel: DevicePickerViewModel = injectViewModel(),
    modifier: Modifier = Modifier,
) {
    val pickerState by viewModel
        .viewState<DevicePickerViewState.Picker>()
        .collectAsState(DevicePickerViewState.Picker.initial)
    var expanded by remember { mutableStateOf(false) }

    var dialogShown by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.wrapContentSize(),
        ) {
            CustomOutlinedTextField(
                value = pickerState.selected?.name ?: "Select device (or refresh first)",
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand menu"
                    )
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                pickerState.devices.forEach { device ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            viewModel.selectDevice(device)
                        }
                    ) {
                        Text(device.name)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = viewModel::refreshDevices,
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = "Refresh available devices",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                dialogShown = true
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.QrCode,
                contentDescription = "Refresh available devices",
                tint = Color.White,
            )
        }
        if (pickerState.selected != null) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = viewModel::run) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Refresh available devices",
                    tint = Color.White,
                )
            }
        }
    }

    if (dialogShown) {
        Dialog(
            onDismissRequest = {
                dialogShown = false
            },
            properties = DialogProperties()
        ) {
            QRPairScreen(
                onDismissRequest = {
                    dialogShown = false
                },
                modifier = Modifier
                    .wrapContentSize()
            )
        }
    }
}
