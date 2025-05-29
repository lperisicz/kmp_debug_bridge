package home

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.outlined.InstallMobile
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberCursorPositionProvider
import components.injectViewModel
import device.DevicePickerScreen
import install.InstallScreen
import notification.NotificationsScreen
import record.RecordScreen
import settings.SettingsScreen

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = injectViewModel(),
    modifier: Modifier = Modifier,
) {
    val navigationState by viewModel
        .viewState<HomeViewState.NavigationSection>()
        .collectAsState(HomeViewState.NavigationSection.initial)

    val contentState by viewModel
        .viewState<HomeViewState.ContentSection>()
        .collectAsState(HomeViewState.ContentSection.initial)

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFF2B2D30))
        ) {
            DevicePickerScreen(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        Row(modifier = Modifier.fillMaxSize()) {
            NavigationSection(
                state = navigationState,
                onNavItemSelected = viewModel::selectScreen,
                modifier = Modifier
                    .background(Color(0xFF2B2D30))
                    .fillMaxHeight()
                    .wrapContentWidth(),
            )
            Spacer(modifier = Modifier.width(1.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                ContentSection(
                    state = contentState,
                    modifier = Modifier
                        .background(Color(0xFF2B2D30))
                        .fillMaxSize(),
                )
                NotificationsScreen(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NavigationSection(
    state: HomeViewState.NavigationSection,
    onNavItemSelected: (HomeViewState.Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(state.navItems) { item ->
            IconToggleButton(
                checked = item.isSelected,
                onCheckedChange = { onNavItemSelected(item.screen) },
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = if (item.isSelected) Color(0xFF4A4B4F) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                val tooltipState = rememberBasicTooltipState(
                    initialIsVisible = false,
                    isPersistent = false,
                )
                val positionProvider = rememberCursorPositionProvider()
                BasicTooltipBox(
                    positionProvider = positionProvider,
                    tooltip = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF4A4B4F))
                                .border(
                                    border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item.screen.name,
                                color = Color.White,
                            )
                        }
                    },
                    state = tooltipState,
                    focusable = true,
                    enableUserInput = true
                ) {
                    Icon(
                        imageVector = item.screen.toIcon(),
                        contentDescription = item.screen.name,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

private fun HomeViewState.Screen.toIcon(): ImageVector =
    when (this) {
        HomeViewState.Screen.Recording -> Icons.Filled.FiberManualRecord
        HomeViewState.Screen.Install -> Icons.Outlined.InstallMobile
        HomeViewState.Screen.Settings -> Icons.Outlined.Settings
    }

@Composable
private fun ContentSection(
    state: HomeViewState.ContentSection,
    modifier: Modifier = Modifier,
) {
    when (state.screen) {
        HomeViewState.Screen.Recording -> RecordScreen(modifier = modifier)
        HomeViewState.Screen.Install -> InstallScreen(modifier = modifier)
        HomeViewState.Screen.Settings -> SettingsScreen(modifier = modifier)
    }
}
