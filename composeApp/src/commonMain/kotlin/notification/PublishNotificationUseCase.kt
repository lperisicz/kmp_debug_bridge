package notification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logger.Logger

internal interface PublishNotificationUseCase {

    suspend operator fun invoke(notificationData: NotificationData)
}

private const val NOTIFICATION_SHOW_DURATION = 10_000L

internal class PublishNotificationUseCaseImpl(
    private val logger: Logger,
    private val appScope: CoroutineScope,
) : PublishNotificationUseCase {

    override suspend fun invoke(notificationData: NotificationData) {
        appScope.launch {
            logger.logI("Publishing notification: $notificationData")

            NotificationsSource.publish(notificationData)
            delay(NOTIFICATION_SHOW_DURATION)
            NotificationsSource.close(notificationId = notificationData.id)
        }
    }
}
