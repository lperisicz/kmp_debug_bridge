package notification

import java.util.UUID

internal data class NotificationData(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val action: Action? = null
) {

    internal sealed class Action(
        val title: String,
    ) {

        data class ShowInDirectory(val filePath: String) : Action(title = "Open")
    }
}
