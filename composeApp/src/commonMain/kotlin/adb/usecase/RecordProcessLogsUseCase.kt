package adb.usecase

import externalcommand.runSuspending
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logger.Logger
import record.GetRecordingSessionUseCase
import record.RecordingSession
import java.io.File

internal interface RecordProcessLogsUseCase {

    suspend operator fun invoke()
}

internal class RecordProcessLogsUseCaseImpl(
    private val logger: Logger,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val getProcessIdUseCase: GetProcessIdUseCase,
) : RecordProcessLogsUseCase {

    override suspend fun invoke() = coroutineScope {
        val session = getRecordingSessionUseCase().first()
        var currentProcessId: String? = null
        var recordingJob: Job? = null
        var processIndex = 0
        while (true) {
            logger.logI("Awaiting processId")
            currentProcessId = awaitDifferentProcess(currentProcessId)
            logger.logI("ProcessId found: $currentProcessId")
            recordingJob?.let {
                logger.logI("Cancelling previous recording job")
                it.cancel()
            }
            logger.logI("Starting new recording job")
            recordingJob = launch {
                logger.logI("Started recording")
                processIndex = processIndex.inc()
                startRecording(currentProcessId, session, processIndex)
            }
            delay(1_000L)
        }
    }

    private suspend fun awaitDifferentProcess(currentProcessId: String?): String {
        while (true) {
            val processId = getProcessIdUseCase()
            if (processId != null && processId != currentProcessId) {
                return processId
            }
            logger.logI("Process id not found: $processId")
            delay(1_000L)
        }
    }

    private suspend fun startRecording(processId: String, session: RecordingSession, processIndex: Int) {
        val path = getOutputPath(session, processIndex)
        val outputFile = File(path)
        return ProcessBuilder("adb", "logcat", "--pid", processId)
            .redirectErrorStream(true)
            .redirectOutput(outputFile)
            .runSuspending()
    }
}

private fun getOutputPath(session: RecordingSession, processIndex: Int) =
    "${session.workspace}/${session.timestamp}/logs-$processIndex.txt"
