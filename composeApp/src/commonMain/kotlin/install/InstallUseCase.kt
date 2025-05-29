package install

import externalcommand.runSuspending
import kotlinx.coroutines.flow.first
import logger.Logger
import record.GetRecordingSessionUseCase

internal interface InstallUseCase {

    suspend operator fun invoke(filePath: String)
}

internal class InstallUseCaseImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val logger: Logger,
) : InstallUseCase {

    override suspend fun invoke(filePath: String) {
        val session = getRecordingSessionUseCase().first()

        if (session.device?.id == null) {
            logger.logE("Failed install, device not connected")
            return
        }

        logger.logI("Installing $filePath to ${session.device.name}")

        ProcessBuilder("adb", "-s", session.device.id, "install", filePath.trim())
            .redirectErrorStream(true)
            .runSuspending()

        logger.logI("Success: App installed")
    }
}
