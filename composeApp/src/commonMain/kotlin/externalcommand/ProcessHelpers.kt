package externalcommand

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal suspend fun ProcessBuilder.runSuspending(): Unit = suspendCancellableCoroutine { continuation ->
    try {
        val process = start()
        continuation.invokeOnCancellation {
            process.destroy()
        }

        process.waitFor()
        if (continuation.isCancelled.not()) {
            continuation.resume(Unit)
        }
    } catch (e: Exception) {
        if (continuation.isCancelled.not()) {
            continuation.resumeWithException(e)
        }
    }
}

internal suspend fun <T> ProcessBuilder.use(block: (Process) -> T): T = suspendCancellableCoroutine { continuation ->
    try {
        val process: Process = start()
        continuation.invokeOnCancellation {
            process.destroy()
        }
        continuation.resume(block(process))
    } catch (e: Exception) {
        continuation.resumeWithException(e)
    }
}
