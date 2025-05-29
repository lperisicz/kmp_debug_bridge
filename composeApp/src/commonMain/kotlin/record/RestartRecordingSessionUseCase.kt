package record

internal interface RestartRecordingSessionUseCase {

    operator fun invoke()
}

internal class RestartRecordingSessionUseCaseImpl(
    private val recordingSessionSource: RecordingSessionSource,
) : RestartRecordingSessionUseCase {

    override fun invoke() = recordingSessionSource.restartTimestamp()
}
