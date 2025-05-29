package install

import adb.usecase.StartAppUseCase
import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import logger.Logger
import picker.chooseFilePath
import record.GetRecordingSessionUseCase
import record.RecordingSession

internal sealed class InstallViewState {

    data class FileState(
        val path: String?,
        val actionEnabled: Boolean,
    ) : InstallViewState() {

        companion object {
            val initial = FileState(
                path = null,
                actionEnabled = false,
            )
        }
    }
}

internal abstract class InstallViewModel : BaseViewModel<InstallViewState>() {

    abstract fun setPath(path: String)

    abstract fun choosePath()

    abstract fun install()
}

private const val APK_PICKER_DESCRIPTION = ".apk Files"
private const val APK_FILE_EXTENSION = "apk"

internal class InstallViewModelImpl(
    private val logger: Logger,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val installUseCase: InstallUseCase,
    private val startAppUseCase: StartAppUseCase,
) : InstallViewModel() {

    private val selectedPath = MutableStateFlow<String?>(null)

    init {
        query {
            combine(
                selectedPath,
                getRecordingSessionUseCase(),
                ::mapToFileState
            )
        }
    }

    override fun setPath(path: String) = selectedPath.update { path }

    override fun choosePath() {
        runCommand {
            val path = chooseFilePath(APK_PICKER_DESCRIPTION, APK_FILE_EXTENSION)
            if (path != null) {
                setPath(path)
            } else {
                logger.logI("Chosen filepath is null")
            }
        }
    }

    override fun install() {
        runCommand {
            val filePath = selectedPath.value
            if (filePath == null) {
                logger.logE("Error installing, chosen filePath is null")
                return@runCommand
            }
            installUseCase(filePath)
            startAppUseCase()
        }
    }
}

private fun mapToFileState(
    filePath: String?,
    recordingSession: RecordingSession,
) = InstallViewState.FileState(
    path = filePath,
    actionEnabled = filePath.isNullOrBlank().not() && recordingSession.device != null
)
