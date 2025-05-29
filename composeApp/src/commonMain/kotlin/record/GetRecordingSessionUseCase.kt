package record

import kotlinx.coroutines.flow.Flow

internal interface GetRecordingSessionUseCase {

    operator fun invoke(): Flow<RecordingSession>
}

internal class GetRecordingSessionUseCaseImpl(
    private val recordingSessionSource: RecordingSessionSource,
) : GetRecordingSessionUseCase {

    override fun invoke(): Flow<RecordingSession> = recordingSessionSource.getSession()
}
