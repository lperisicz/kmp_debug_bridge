package record

import core.viewModel.BaseViewModel
import io.usecase.PackageSessionDirectoryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import logger.Logger

internal sealed class RecordViewState {

    data class Recording(
        val active: Boolean,
    ) : RecordViewState() {

        companion object {
            val initial = Recording(false)
        }
    }
}

internal abstract class RecordViewModel : BaseViewModel<RecordViewState>() {

    abstract fun toggleRecording()
}

internal class RecordViewModelImpl(
    private val startRecordingUseCase: StartRecordingUseCase,
    private val packageSessionDirectoryUseCase: PackageSessionDirectoryUseCase,
    private val logger: Logger,
) : RecordViewModel() {

    private val recordingInProgress = MutableStateFlow(false)
    private var recordingJob: Job? = null

    init {
        query { recordingInProgress.map(RecordViewState::Recording) }
    }

    override fun toggleRecording() {
        runCommand {
            if (recordingInProgress.first()) {
                stopRecording()
            } else {
                startRecording()
            }
            recordingInProgress.update { it.not() }
        }
    }

    private fun startRecording() {
        stopRecording()
        recordingJob = runCommand { startRecordingUseCase() }
    }

    private fun stopRecording() = recordingJob?.let {
        if (it.isActive) {
            logger.logI("Stopped recording")
            it.cancel()
            runCommand {
                packageSessionDirectoryUseCase()
                logger.logI("----------------------------")
            }
        }
    }
}
