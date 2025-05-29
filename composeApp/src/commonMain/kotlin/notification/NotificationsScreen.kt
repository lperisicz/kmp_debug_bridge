package notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import components.injectViewModel

@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsViewModel = injectViewModel(),
    modifier: Modifier = Modifier,
) {
    val notificationsState by viewModel
        .viewState<NotificationsViewState.Notifications>()
        .collectAsState(NotificationsViewState.Notifications.initial)

    LazyColumn(
        reverseLayout = true,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        items(
            items = notificationsState.items,
            key = NotificationData::id,
        ) { item ->
            NotificationCard(
                notificationData = item,
                onClose = viewModel::close,
                onAction = viewModel::performAction,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .widthIn(max = 300.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NotificationCard(
    notificationData: NotificationData,
    onClose: (notificationId: String) -> Unit,
    onAction: (id: String, action: NotificationData.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isHoveredOver by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0xFF4A4B4F),
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .onPointerEvent(PointerEventType.Enter) { isHoveredOver = true }
            .onPointerEvent(PointerEventType.Exit) { isHoveredOver = false },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Top)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = notificationData.message,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(4.dp))

                AnimatedVisibility(
                    visible = isHoveredOver,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .clickable { onClose(notificationData.id) }
                    )
                }
            }
            if (notificationData.action != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .clickable { onAction(notificationData.id, notificationData.action) }
                        .align(Alignment.End)
                ) {
                    Text(
                        text = notificationData.action.title,
                        color = Color(0xFF6C9BFA),
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }
}
