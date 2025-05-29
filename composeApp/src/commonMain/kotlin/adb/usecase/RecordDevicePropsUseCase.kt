package adb.usecase

import externalcommand.runSuspending
import kotlinx.coroutines.flow.first
import logger.Logger
import record.GetRecordingSessionUseCase
import record.RecordingSession
import java.io.File

internal interface RecordDevicePropsUseCase {

    suspend operator fun invoke()
}

internal class RecordDevicePropsUseCaseImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val logger: Logger,
) : RecordDevicePropsUseCase {

    override suspend fun invoke() {
        val session = getRecordingSessionUseCase().first()
        val outputFile = getOutputFile(session)

        logger.logI("Started recording getprop to ${outputFile.path}")

        return ProcessBuilder("adb", "shell", "getprop")
            .redirectErrorStream(true)
            .redirectOutput(outputFile)
            .runSuspending()
    }
}

private fun getOutputFile(session: RecordingSession) = File("${session.workspace}/${session.timestamp}/props.txt")
