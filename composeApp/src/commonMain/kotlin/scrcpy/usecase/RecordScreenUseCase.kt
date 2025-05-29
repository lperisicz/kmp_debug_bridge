package scrcpy.usecase

import externalcommand.runSuspending
import kotlinx.coroutines.flow.first
import record.GetRecordingSessionUseCase
import record.RecordingSession

internal interface RecordScreenUseCase {

    suspend operator fun invoke()
}

internal class RecordScreenUseCaseImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
) : RecordScreenUseCase {

    override suspend fun invoke() {
        val session = getRecordingSessionUseCase().first()
        val filePath = getOutputFilePath(session)
        ProcessBuilder("scrcpy", "--no-playback", "--record=$filePath")
            .redirectErrorStream(true)
            .runSuspending()
    }
}

private fun getOutputFilePath(session: RecordingSession) = "${session.workspace}/${session.timestamp}/screen_recording.mp4"
