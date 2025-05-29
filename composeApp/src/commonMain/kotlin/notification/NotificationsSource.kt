package notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull

internal object NotificationsSource {

    private val notificationsPublisher = MutableStateFlow<List<NotificationData>>(emptyList())

    fun data(): Flow<List<NotificationData>> = notificationsPublisher

    suspend fun publish(notificationData: NotificationData) {
        val existing = notificationsPublisher.value
        val newList = existing + notificationData
        notificationsPublisher.emit(newList)
    }

    suspend fun close(notificationId: String) {
        val existing = notificationsPublisher.firstOrNull() ?: emptyList()
        val newList = existing.filterNot { it.id == notificationId }
        notificationsPublisher.emit(newList)
    }
}
