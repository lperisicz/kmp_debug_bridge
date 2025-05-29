package adb.usecase

import externalcommand.runSuspending
import kotlinx.coroutines.flow.first
import logger.Logger
import record.GetRecordingSessionUseCase
import record.RecordingSession
import java.io.File

internal interface RecordDumpSysUseCase {

    suspend operator fun invoke()
}

internal class RecordDumpSysUseCaseImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val logger: Logger,
) : RecordDumpSysUseCase {

    override suspend fun invoke() {
        val session = getRecordingSessionUseCase().first()
        val outputFile = getOutputFile(session)

        logger.logI("Started recording dumpsys to ${outputFile.path}")

        return ProcessBuilder("adb", "shell", "dumpsys", "package", session.packageName)
            .redirectErrorStream(true)
            .redirectOutput(outputFile)
            .runSuspending()
    }
}

private fun getOutputFile(session: RecordingSession) = File("${session.workspace}/${session.timestamp}/dumpsys.txt")
