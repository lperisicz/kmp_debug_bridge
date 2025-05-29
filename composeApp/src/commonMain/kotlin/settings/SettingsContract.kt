package settings

import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logger.Logger
import picker.chooseDirectory
import record.GetRecordingSessionUseCase
import record.RecordingSession

internal sealed class SettingsViewState {

    data class Input(
        val packageName: String,
        val workspace: String,
    ) : SettingsViewState() {

        companion object {
            val initial = Input(
                packageName = "",
                workspace = "",
            )
        }
    }
}

internal abstract class SettingsViewModel : BaseViewModel<SettingsViewState>() {

    abstract fun setPackageName(packageName: String)

    abstract fun setWorkspace(workspace: String)

    abstract fun chooseWorkspace()
}

internal class SettingsViewModelImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val setSessionWorkspaceUseCase: SetSessionWorkspaceUseCase,
    private val setSessionPackageNameUseCase: SetSessionPackageNameUseCase,
    private val logger: Logger,
) : SettingsViewModel() {

    init {
        query { getRecordingSessionUseCase().mapToWorkspaceState() }
    }

    override fun setPackageName(packageName: String) {
        runCommand {
            setSessionPackageNameUseCase(packageName)
        }
    }

    override fun setWorkspace(workspace: String) {
        runCommand {
            setSessionWorkspaceUseCase(workspace)
        }
    }

    override fun chooseWorkspace() {
        runCommand {
            val directory = chooseDirectory()
            if (directory != null) {
                setSessionWorkspaceUseCase(directory)
            } else {
                logger.logI("Chosen directory is null")
            }
        }
    }
}

private fun Flow<RecordingSession>.mapToWorkspaceState(): Flow<SettingsViewState.Input> =
    map { session ->
        SettingsViewState.Input(
            packageName = session.packageName,
            workspace = session.workspace
        )
    }
