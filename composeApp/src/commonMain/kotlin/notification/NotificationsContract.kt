package notification

import core.viewModel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import picker.showFile

internal sealed class NotificationsViewState {

    data class Notifications(
        val items: List<NotificationData>,
    ) : NotificationsViewState() {

        companion object {
            val initial = Notifications(items = emptyList())
        }
    }
}

internal abstract class NotificationsViewModel : BaseViewModel<NotificationsViewState>() {

    abstract fun close(notificationId: String)

    abstract fun performAction(notificationId: String, action: NotificationData.Action)
}

private const val NOTIFICATION_CLOSE_DELAY = 300L

internal class NotificationsViewModelImpl : NotificationsViewModel() {

    init {
        query { NotificationsSource.data().mapToNotificationsState() }
    }

    override fun close(notificationId: String) {
        runCommand { NotificationsSource.close(notificationId) }
    }

    override fun performAction(notificationId: String, action: NotificationData.Action) {
        runCommand {
            when (action) {
                is NotificationData.Action.ShowInDirectory -> showFile(action.filePath)
            }
            delay(NOTIFICATION_CLOSE_DELAY)
            close(notificationId)
        }
    }
}

private fun Flow<List<NotificationData>>.mapToNotificationsState(): Flow<NotificationsViewState.Notifications> =
    map(NotificationsViewState::Notifications)
