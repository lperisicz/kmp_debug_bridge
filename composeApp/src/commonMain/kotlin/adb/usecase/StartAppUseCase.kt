package adb.usecase

import externalcommand.runSuspending
import externalcommand.use
import kotlinx.coroutines.flow.first
import logger.Logger
import record.GetRecordingSessionUseCase
import java.io.BufferedReader
import java.io.InputStreamReader

internal interface StartAppUseCase {

    suspend operator fun invoke()
}

internal class StartAppUseCaseImpl(
    private val logger: Logger,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
) : StartAppUseCase {

    override suspend fun invoke() {
        val session = getRecordingSessionUseCase().first()
        val deviceId = session.device?.id

        if (deviceId == null) {
            logger.logE("Failed to start the app, no device connected")
            return
        }

        val launcherActivity = ProcessBuilder(
            "adb", "-s", deviceId, "shell", "cmd", "package",
            "resolve-activity", "--brief", session.packageName
        )
            .redirectErrorStream(true)
            .use { process ->
                BufferedReader(InputStreamReader(process.inputStream)).readText()
            }
            .trim()
            .lines()
            .lastOrNull()

        if(launcherActivity == null) {
            logger.logE("Unable to find launcher activity for ${session.packageName}")
            return
        }

        logger.logI("Starting the app: ${session.packageName} - $launcherActivity")

        ProcessBuilder("adb", "-s", deviceId, "shell", "am", "start", "-n", launcherActivity)
            .redirectErrorStream(true)
            .runSuspending()
    }
}
