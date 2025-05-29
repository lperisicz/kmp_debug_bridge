package components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun CustomOutlinedTextField(
    value: String,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit = { },
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = Color(0xFFFFFFFF),
    ),
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var fieldValue by remember(value) { mutableStateOf(value) }

    OutlinedTextField(
        value = fieldValue,
        readOnly = readOnly,
        onValueChange = { newValue ->
            fieldValue = newValue
            onValueChange(newValue)
        },
        trailingIcon = trailingIcon,
        colors = colors,
        singleLine = singleLine,
        label = label,
        modifier = modifier
    )
}
