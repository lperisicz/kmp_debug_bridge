package settings

import record.RecordingSessionSource

internal interface SetSessionWorkspaceUseCase {

    operator fun invoke(workspace: String)
}

internal class SetSessionWorkspaceUseCaseImpl(
    private val recordingSessionSource: RecordingSessionSource,
) : SetSessionWorkspaceUseCase {

    override fun invoke(workspace: String) = recordingSessionSource.setWorkspace(workspace)
}
